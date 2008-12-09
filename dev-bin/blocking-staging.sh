#!/bin/bash

# complet runtime is 6s * DUR
DUR=10

i=0
while [ "$i" -le "$DUR"  ]; do
    echo -n z
    sleep 6
done
