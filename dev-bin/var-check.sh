var_check ( ) {
    if [ -z $GNDMS_SOURCE ]; then
        echo "Please set the GNDMS_SOURCE variable to the root"
        echo "directory of the GNDMS software distribution."
        exit 1
    fi
}
