
# Calculates the release candidate version number for a SNAPSHOT release

DESCRIBE=$(git describe)

if [ -z "$DESCRIBE" ]; then
  export RC_VERSION=$(git rev-list --count HEAD)
else
  export RC_VERSION=$(echo $DESCRIBE | cut -d - -f 2)
fi

echo "RC_VERSION: $RC_VERSION"
