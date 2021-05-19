SUMMARY = "IVAS accel sw libs"
DESCRIPTION = "IVAS accelerator libraries"
SECTION = "multimedia"
LICENSE = "Apache-2.0"

include uncanny-ivas.inc

SRC_URI += " \
  file://0001-Add-ALPR-and-Vehicle-detection-model-support.patch \
  file://0001-Update-build-dependencies-for-ivas-accel-sw-libs.patch \
  "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

DEPENDS = "glib-2.0 glib-2.0-native xrt libcap libxml2 bison-native flex-native jansson ivas-utils uncanny-ivas-gst opencv vitis-ai-library vart uncanny-tvm"
RDEPENDS_${PN} = "uncanny-tvm"

inherit meson pkgconfig gettext

LIC_FILES_CHKSUM = "file://../LICENSE;md5=e6d9577dd6743c14fb3056b97887d4a4"

S = "${WORKDIR}/ivas/ivas-accel-sw-libs"
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
  export PKG_CONFIG_PATH="${STAGING_DIR_HOST}${opt_prefix}/lib/pkgconfig:${PKG_CONFIG_PATH}"
  meson --prefix ${opt_prefix} --buildtype plain               --bindir bin               --sbindir sbin               --datadir share               --libdir lib               --libexecdir libexec               --includedir include               --mandir share/man               --infodir share/info               --sysconfdir /etc               --localstatedir /var               --sharedstatedir /com --wrap-mode nodownload "${WORKDIR}/ivas/ivas-accel-sw-libs" "${WORKDIR}/build" --cross-file ${WORKDIR}/meson.cross
}

do_install_append() {
  chrpath -r ${opt_prefix}/lib ${D}${opt_prefix}/lib/libivas_xdpuinfer.so
}

SYSROOT_DIRS += "${opt_prefix}/lib"

FILES_${PN} += "${opt_prefix}/lib/ivas/*.so ${opt_prefix}/lib/*.so"
#FILES_${PN}-dev += "${opt_prefix}/include/"
