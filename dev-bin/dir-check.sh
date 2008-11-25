
dir_check() {
    if [ -e "$1"  ]
    then
        if [ -d "$1" ] 
        then
            while [[ "$c" != "y" && "$c" != "n" ]]
            do
                echo "\"$1\" exists. Proceed? (y/n)"
                read c
            done
            if [[ "$c" == "n" ]]
            then
                echo "Aborting..."
                exit 1
            fi
        else
            echo 
            echo "$1 exists and isn't a directory."
            echo 
        fi
    else 
        mkdir -p $1
    fi
}
