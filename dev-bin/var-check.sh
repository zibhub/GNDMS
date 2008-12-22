var_check ( ) {
    if [ -z $C3GRID_SOURCE ]; then
        echo "Please set C3GRID_SOURCE variable to the root"
        echo "directory of the C3Grid DMS distribution."
        exit 1
    fi
}
