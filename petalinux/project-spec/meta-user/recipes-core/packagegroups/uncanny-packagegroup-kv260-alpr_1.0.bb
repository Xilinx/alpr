DESCRIPTION = "Smartcamera ALPR related Packages"

inherit packagegroup

PR = "1.pl2020_2_2"

SMARTCAM_ALPR_PACKAGES = " \
	gst-perf \
	gstreamer1.0-omx \
	gstreamer1.0-plugins-good-rtp \
	gstreamer1.0-plugins-bad-kms \
	gstreamer1.0-plugins-bad-mediasrcbin \
	gstreamer1.0-plugins-bad-videoparsersbad \
  gstreamer1.0-plugins-bad-debugutilsbad \
	gstreamer1.0-plugins-good-multifile \
	gstreamer1.0-plugins-good-rtpmanager \
  gstreamer1.0-plugins-good-rtsp \
	gstreamer1.0-plugins-good-udp \
  gstreamer1.0-plugins-good-isomp4 \
  gstreamer1.0-plugins-good-avi \
	gstreamer1.0-plugins-good-video4linux2 \
	gstreamer1.0-python \
	gstreamer1.0-rtsp-server \
	libdrm-tests \
	v4l-utils \
  uncanny-kv260-smartcam-alpr \
  uncanny-pyxir \
  uncanny-tvm \
  uncanny-alpr-model \
  ivas-utils \
  uncanny-ivas-gst \
  uncanny-ivas-accel-libs \
  docker-ce \
  python3-docker-compose \
	"

RDEPENDS_${PN} = "${SMARTCAM_ALPR_PACKAGES}"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_k26-kv = "${MACHINE}"
PACKAGE_ARCH = "${BOARDVARIANT_ARCH}"
