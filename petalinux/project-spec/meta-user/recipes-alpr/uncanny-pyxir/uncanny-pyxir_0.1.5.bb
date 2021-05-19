SUMMARY = "PyXIR is an Neural Network Intermediate Representation (IR) for deep learning"
DESCRIPTION = "PyXIR is an Neural Network Intermediate Representation (IR) for deep learning"
LICENSE = "Apache-2.0"

PR = "1.pl2020_2_2"

BRANCH ?= "master"
REPO   ?= "gitsm://github.com/Xilinx/pyxir.git;protocol=https"
SRCREV = "c424395daf64a80b1478c20cb9283079929df461"

SRC_URI = "${REPO};branch=master"

SRC_URI[md5sum] = "0556f9cebc16df1b723b6cc10550e8ab"
SRC_URI[sha256sum] = "34db52232f7f2f1d26f8ee08a713b44a6cf56151143ede484721ec5eff75cb54"

LIC_FILES_CHKSUM = "file://LICENSE;md5=9ecce103fa3ee74c5a0cda57238285a6"

S  = "${WORKDIR}/git"
opt_prefix = "/opt/uncanny"

DEPENDS = "cmake-native dnndk"

DISTUTILS_BUILD_ARGS ?= "--use_vai_rt_dpuczdx8g"
DISTUTILS_INSTALL_ARGS ?= "--use_vai_rt_dpuczdx8g --prefix=${D}${opt_prefix}"

inherit distutils3 setuptools3 python3-dir

RDEPENDS_${PN} = "python3-cached-property python3-numpy python3-h5py python3-pydot python3-pyparsing dnndk"

TARGET_ARCH = "aarch64"
HOST_ARCH = "x86_64"

do_configure_prepend() {
  cd ${S}
  sed -i "s+set(vitisai_LIBRARIES /usr/lib)+set(vitisai_LIBRARIES ${STAGING_LIBDIR})+g" ${S}/CMakeLists.txt
  sed -i "s+set(vitisai_INCLUDE_DIRS /usr/include/vai)+set(vitisai_INCLUDE_DIRS ${STAGING_INCDIR}/vai)+g" ${S}/CMakeLists.txt
  sed -i "s+'numpy'+''+g" ${S}/setup.py
  sed -i "s+'h5py>=2.8.0'+''+g" ${S}/setup.py
  sed -i "s+'pydot==1.4.1'+''+g" ${S}/setup.py
}

OPT_PYTHON_SITEPACKAGES_DIR = "${opt_prefix}/lib/${PYTHON_DIR}/site-packages"

do_install() {
  install -d ${D}${OPT_PYTHON_SITEPACKAGES_DIR}
  STAGING_INCDIR=${STAGING_DIR_HOST}/usr/include \
  STAGING_LIBDIR=${STAGING_DIR_HOST}/usr/lib \
  PYTHONPATH=${D}${OPT_PYTHON_SITEPACKAGES_DIR} \
  ${PYTHON} setup.py install ${DISTUTILS_INSTALL_ARGS}
  
  mv ${D}${OPT_PYTHON_SITEPACKAGES_DIR}/pyxir-${PV}-py${PYTHON_BASEVERSION}-linux-${HOST_ARCH}.egg ${D}${OPT_PYTHON_SITEPACKAGES_DIR}/pyxir-${PV}-py${PYTHON_BASEVERSION}-linux-${TARGET_ARCH}.egg
  cd ${D}${OPT_PYTHON_SITEPACKAGES_DIR}
  rm easy-install.pth
  echo "./pyxir-${PV}-py${PYTHON_BASEVERSION}-linux-${TARGET_ARCH}.egg" >> easy-install.pth
}

SYSROOT_DIRS += "${opt_prefix}/lib"
FILES_${PN} += "${opt_prefix}/lib/*"

