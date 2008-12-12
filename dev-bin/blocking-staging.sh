#!/bin/bash

# complet runtime is 5s * DUR

if [ "$#" -ne 1 ]; then
    DUR=12
else 
    DUR=$1
fi

i=0
while [ "$i" -lt "$DUR"  ]; do
    echo -n z
    sleep 5
    let i++
done
