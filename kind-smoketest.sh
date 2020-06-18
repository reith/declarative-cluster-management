!/bin/bash

# standard bash error handling
set -o errexit;
set -o pipefail;
set -o nounset;
# debug commands
set -x;

# working dir to install binaries etc, cleaned up on exit
BIN_DIR="$(mktemp -d)"
# kind binary will be here
KIND="${BIN_DIR}/kind"

# cleanup on exit (useful for running locally)
cleanup() {
    "${KIND}" delete cluster || true
    rm -rf "${BIN_DIR}"
}
trap cleanup EXIT

# util to install a released kind version into ${BIN_DIR}
install_kind_release() {
    VERSION="v0.5.1"
    KIND_BINARY_URL="https://github.com/kubernetes-sigs/kind/releases/download/${VERSION}/kind-linux-amd64"
    wget -O "${KIND}" "${KIND_BINARY_URL}"
    chmod +x "${KIND}"
}

main() {
    # get kind
    install_kind_release
    # create a cluster
    "${KIND}" create cluster --config k8s-scheduler/src/test/resources/kind-test-cluster-configuration.yaml --name dcm-it
    # set KUBECONFIG to point to the cluster
    KUBECONFIG="$("${KIND}" get kubeconfig-path)"
    export KUBECONFIG
    # TODO: invoke your tests here
    # teardown will happen automatically on exit

    export PATH=/opt/gradle/gradle-6.5/bin:$PATH
    export PATH=~/jdk-12.0.2/bin:$PATH
    export PATH=~/MiniZincIDE-2.3.2-bundle-linux/bin/:$PATH
    export JAVA_HOME=~/jdk-12.0.2/
    export OR_TOOLS_LIB=~/ortools/lib/libjniortools.so
    export LD_LIBRARY_PATH=~/MiniZincIDE-2.3.2-bundle-linux/lib
    export QT_PLUGIN_PATH=~/MiniZincIDE-2.3.2-bundle-linux/plugins
    
    KUBECONFIG=~/.kube/kind-config-dcm-it ./gradelw test integrationTest jacocoTestReport
    bash <(curl -s https://codecov.io/bash)
}

main
