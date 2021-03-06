#
# Copyright 2018 George Aristy
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


# Calculates the release candidate version number for a SNAPSHOT release

DESCRIBE=$(git describe)

if [ -z "$DESCRIBE" ]; then
  export RC_VERSION=$(git rev-list --count HEAD)
else
  export RC_VERSION=$(echo $DESCRIBE | cut -d - -f 2)
fi

echo "RC_VERSION: $RC_VERSION"
