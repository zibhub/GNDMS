#!/bin/sh

echo "Cleaning up .GARBAGE"
rm -f .GARBAGE
rm -f /tmp/fooadsf

# Uncomment to test failure of cancel-script
# echo "Ooops" >&2
# exit 1

exit 0
