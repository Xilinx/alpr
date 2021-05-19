SUMMARY = "IVAS gst"
DESCRIPTION = "IVAS gstreamer plugins for IVAS SDK"
SECTION = "multimedia"
LICENSE = "Apache-2.0 & LGPLv2 & MIT & BSD-3-Clause"

include uncanny-ivas.inc

SRC_URI += " \
  file://0001-Add-zmqsink-plugin-support-for-ALPR-docker-interface.patch \
  file://0002-Add-ALPR-to-ivas-model-class.patch \
  file://0001-Add-pkgconfig-file-for-ivas-gst-plugins.patch \
  "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS = "glib-2.0 glib-2.0-native xrt libcap libxml2 bison-native flex-native gstreamer1.0 jansson ivas-utils cppzmq"

RDEPENDS_${PN} = "gstreamer1.0-plugins-base"

inherit meson pkgconfig gettext

LIC_FILES_CHKSUM = "file://../LICENSE;md5=e6d9577dd6743c14fb3056b97887d4a4"

S = "${WORKDIR}/ivas/ivas-gst-plugins"
opt_prefix = "/opt/uncanny"

GIR_MESON_ENABLE_FLAG = "enabled"
GIR_MESON_DISABLE_FLAG = "disabled"

override_native_tools() {
    # Set these so that meson uses the native tools for its build sanity tests,
    # which require executables to be runnable. The cross file will still
    # override these for the target build.
    export CC="gcc "
    export CXX="g++ "
    export LD="ld "
    export AR="ar"
    export STRIP="strip"
    # These contain *target* flags but will be used as *native* flags.  The
    # correct native flags will be passed via -Dc_args and so on, unset them so
    # they don't interfere with tools invoked by Meson (such as g-ir-scanner)
    unset CPPFLAGS CFLAGS CXXFLAGS LDFLAGS
}

do_configure() {
  override_native_tools
  meson --prefix ${opt_prefix} --buildtype plain               --bindir bin               --sbindir sbin               --datadir share               --libdir lib               --libexecdir libexec               --includedir include               --mandir share/man               --infodir share/info               --sysconfdir /etc               --localstatedir /var               --sharedstatedir /com --wrap-mode nodownload "${WORKDIR}/ivas/ivas-gst-plugins" "${WORKDIR}/build" --cross-file ${WORKDIR}/meson.cross
}

SYSROOT_DIRS += "${opt_prefix}"

FILES_${PN} += "${opt_prefix}/lib/gstreamer-1.0/*.so ${opt_prefix}/lib/*.so.* ${opt_prefix}/include/* ${opt_prefix}/lib/pkgconfig/*"
FILES_${PN}-dev += "${opt_prefix}/include/gstreamer-1.0/* ${opt_prefix}/lib/*.so"
