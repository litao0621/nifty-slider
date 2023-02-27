set -ex

PROPERTY_FILE=gradle.properties

function getProperty {
   PROP_KEY=$1
   PROP_VALUE=$(cat $PROPERTY_FILE | grep "$PROP_KEY" | cut -d'=' -f2)
   echo "$PROP_VALUE"
}

echo "# Reading property from $PROPERTY_FILE"

PUBLISH_VERSION=$(getProperty "PUBLISH_VERSION_NAME")

git tag "v$PUBLISH_VERSION"

git push origin "v$PUBLISH_VERSION"
