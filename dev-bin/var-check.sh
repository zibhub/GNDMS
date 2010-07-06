var_check ( ) {
    if [ -z $GNDMS_SOURCE ]; then
        echo "Please set GNDMS_SOURCE variable to the root"
        echo "directory of the C3Grid DMS distribution."
        exit 1
    fi
}
