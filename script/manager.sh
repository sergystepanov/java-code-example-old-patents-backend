#!/bin/bash
#
# Download a release asset from a Github release.
#
# REQUIRED: curl
#
# USAGE
#
# Set all the variables inside the script, make sure you chmod +x it, then
# to download specific version to my_app.tar.gz:
#
#     manager -u user -t token123 -r developer/app
#

while [[ "$#" > 0 ]]; do case $1 in
    -u|--u) user="$2"; shift;;
	  -t|--t) token="$2"; shift;;
	  -r|--repo) repo="$2"; shift;;
    -c|--check) check=1;;
    *) echo "Unknown parameter passed: $1"; exit 1;;
esac; shift; done

# Make a request for releases
asset=$(curl -u $user:$token -s https://api.github.com/repos/$repo/releases/latest \
	| grep '"url".*/assets' \
	| cut -d '"' -f 4)

echo "Downloading the latest release ${asset##*/} from $repo"

curl -# -L -o latest.tar \
	--header "Authorization: token $token" \
	--header 'Accept: application/octet-stream' \
	https://$token:@api.github.com/repos/$repo/releases/assets/${asset##*/}
