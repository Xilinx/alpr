SUMMARY = "TVM is an open source machine learning compiler framework"
DESCRIPTION = "Apache TVM is an open source machine learning compiler framework for CPUs, GPUs, and machine learning accelerators."
LICENSE = "Apache-2.0"

PR = "1.pl2020_2_2"

BRANCH ?= "main"
REPO   ?= "gitsm://github.com/apache/incubator-tvm.git;protocol=https"
SRCREV = "51dc332646d90b77ae29e2e2dbe21f40008a0082"

SRC_URI = "${REPO};branch=main"
SRC_URI += " \
            file://remove-pyxir-import.patch \
            file://tvm-runtime.pc \
            "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LIC_FILES_CHKSUM = "file://LICENSE;md5=e313a9b6eda820e35716d9529001537f"

S  = "${WORKDIR}/git"


DEPENDS = "uncanny-pyxir"
RDEPENDS_${PN} = "uncanny-pyxir"

inherit cmake python3-dir

TARGET_ARCH = "aarch64"
PYXIR_VER = "0.1.5"
opt_prefix = "/opt/uncanny"
OPT_PYTHON_SITEPACKAGES_DIR = "${opt_prefix}/lib/${PYTHON_DIR}/site-packages"

do_configure() {
	cd ${S}
  mkdir -p build
  cp ${S}/cmake/config.cmake ${S}/build/
  cd ${S}/build/
  echo set\(USE_VITIS_AI ON\) >> config.cmake
  echo set\(USE_LLVM OFF\) >> config.cmake
  
  sed -i 's+${PYXIR_INCLUDE_DIR}+PYXIR_INCLUDE_DIR+g' ${S}/cmake/modules/contrib/VitisAI.cmake
  sed -i 's+${PYXIR_LIB_DIR}+PYXIR_LIB_DIR+g' ${S}/cmake/modules/contrib/VitisAI.cmake
  sed -i "s+(PYXIR_INCLUDE_DIR)+(${STAGING_DIR_HOST}${OPT_PYTHON_SITEPACKAGES_DIR}\/pyxir-${PYXIR_VER}-py${PYTHON_BASEVERSION}-linux-${TARGET_ARCH}.egg\/pyxir\/include\/)+g" ${S}/cmake/modules/contrib/VitisAI.cmake
  sed -i "s+(PYXIR_LIB_DIR)+(${STAGING_DIR_HOST}${OPT_PYTHON_SITEPACKAGES_DIR}\/pyxir-${PYXIR_VER}-py${PYTHON_BASEVERSION}-linux-${TARGET_ARCH}.egg\/)+g" ${S}/cmake/modules/contrib/VitisAI.cmake
  
  PYTHONPATH=${STAGING_DIR_HOST}${OPT_PYTHON_SITEPACKAGES_DIR} \
  cmake -G 'Ninja' -DCMAKE_MAKE_PROGRAM=ninja -DCMAKE_TOOLCHAIN_FILE=${S}/../toolchain.cmake ..
}


do_compile() {
  cd ${S}/build
  ninja tvm_runtime
}

do_install() {
  cd ${S}
  install -d ${D}${opt_prefix}/lib
  install -d ${D}${opt_prefix}/lib/pkgconfig
  install -d ${D}${opt_prefix}/include
  cp -rf ${WORKDIR}/tvm-runtime.pc ${D}${opt_prefix}/lib/pkgconfig
  cp -rf ./3rdparty/dlpack/include/dlpack ${D}${opt_prefix}/include
  cp -rf ./3rdparty/dmlc-core/include/dmlc ${D}${opt_prefix}/include
  cp -rf ./include/tvm ${D}${opt_prefix}/include
  
  cd ${S}/build
  install libtvm_runtime.so ${D}${opt_prefix}/lib
  chrpath -r ${OPT_PYTHON_SITEPACKAGES_DIR}/pyxir-${PYXIR_VER}-py${PYTHON_BASEVERSION}-linux-${TARGET_ARCH}.egg ${D}${opt_prefix}/lib/libtvm_runtime.so
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
SYSROOT_DIRS += "${opt_prefix}"
FILES_${PN} += "${opt_prefix}"
