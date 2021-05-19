#
# This file is the kv260-smartcamera-alpr firmware recipe.
#

inherit fpgamanager_custom

LICENSE = "Proprietary & Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://${THISDIR}/files/LICENSE-BINARIES;md5=de4182ad526ebdc474e232665a902705 \
	file://${THISDIR}/files/LICENSE-APACHEv2;md5=f5add19ac87c8b241c63ef655cdef05c \
	"


FPGA_MNGR_RECONFIG_ENABLE = "1"

SRC_URI = "file://kv260-smartcamera-alpr.bit \
           file://kv260-smartcamera-alpr.dtsi \
           file://kv260-smartcamera-alpr.xclbin \
           "

S = "${WORKDIR}"

PR = "1.pl2020_2_2"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE_k26-kv = "${MACHINE}"
PACKAGE_ARCH = "${BOARDVARIANT_ARCH}"

