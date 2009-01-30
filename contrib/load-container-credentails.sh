grid-proxy-init -key /etc/grid-security/containerkey.pem -cert /etc/grid-security/containercert.pem -out containerproxy.pem
export X509_USER_PROXY=containerproxy.pem
