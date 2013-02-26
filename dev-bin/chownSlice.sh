#!/bin/bash

GRID_MAPFILE="/etc/grid-security/grid-mapfile"
CHOWN=/bin/chown
CHMOD=/bin/chmod
GREP=/bin/grep
BASENAME=/bin/basename
DIRNAME=/usr/bin/dirname
PROGRAM=$( $BASENAME $0 )
SERVLETUSER="tomcat"

# Alternative zum festen Eintragen des Usernamens an dieser Stelle: Sourcen
# einer Datei, in der SERVLETUSER definiert wird.  Das ist aber gefaehrlich,
# weil in der gesourcten Datei prinzipell auch beliebiger Code drinstehen
# koennte, der dann hier mit Root-Rechten ausgefuehrt wird.

FIXEDBASEDIR="/projects/c3storage/dms_workspace"

failWith() {
    ec=$1
    shift
    msg="$1"

    echo $msg
    # Evtl waere es sinnvoller folgendes zu verwenden: echo "$msg" >&2
    # Auf diesem Weg wuerde die Standardfehlerausgabe genutzt werden (und keine Substitution durch die Shell zugelassen).
    exit $ec 
}

if [ "$#" -ne "3" ]; then 
    failWith 1 "Wrong number of arguments, expected 3 received $#"
fi

uid="$1"
baseDir="$2"
sliceId="$3"

if [ -n "$FIXEDBASEDIR" ]
then
    # FIXEDBASEDIR ist gesetzt und kann daher auch verwendet werden.
    # Das ist die sicherere Variante.
    #subSpace=$( $DIRNAME "$baseDir" )
    subSpace=${baseDir%/*}
    # Der letzte Teil der Pfadkomponente, also "RW" oder "CP", wird
    # abgeschnitten.  Was uebrig bleibt, sollte dem Verzeichnis entsprechen,
    # in dem die unterschiedlichen Slice-Kinds liegen.
    if [ "$subSpace" != "$FIXEDBASEDIR" ]
    then
        failWith 5 "Directory $baseDir is not within $FIXEDBASEDIR, aborting"
    fi
else
    :
    # Die unsichere Variante: Da das Basisverzeichnis nicht bekannt ist,
    # wird ohne weitere Pruefung die Angabe von extern (Tomcat) uebernommen.
fi

sliceDir="$baseDir/$sliceId"

if echo "$sliceId" | $GREP -q '\.\.' 
then
    failWith 3 "Detected directory traversal attempt using sliceId $sliceId"
    ## es wurde versucht chownSlice in einem Verzeichnis a la
    ## /projects/c3storage/dms_workspace/RW/../../../foo/bar auszufuehren, also
    ## ausserhalb des vom DMS verwalteten Bereichs. Das ist nicht erlaubt!
fi

if [ ! -d "$sliceDir" ]; then 
    failWith 2 "Folder $sliceDir doesn't exist"
fi

if echo "$uid" | $GREP -E -q '^[a-zA-Z0-9_\-]{2,20}$'
then
    :
    # Username besteht nur aus sinnvoller Menge sinnvoller Zeichen; es ist alles OK.
else
    failWith 4 "Invalid username: $uid"
fi



if [ -n "$SERVLETUSER" -a "$SERVLETUSER" = "$uid" ]
then
    echo "$PROGRAM accepting username $uid because it is the username of the servlet engine"
elif $GREP -q $GRID_MAPFILE -e "\b$uid\b"
then
    echo "$PROGRAM accepting username $uid because it is contained in gridmapfile"
else
    failWith 6 "$uid is neither the servlet engine's username ($SERVLETUSER) nor in $GRID_MAPFILE"
fi

echo "$PROGRAM calling $CHOWN -R $uid $sliceDir"
$CHOWN -R "$uid" "$sliceDir"
echo "$PROGRAM calling $CHMOD g+rx $sliceDir"
$CHMOD g+rx "$sliceDir"
