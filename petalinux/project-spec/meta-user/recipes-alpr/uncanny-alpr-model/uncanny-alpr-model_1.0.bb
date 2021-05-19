SUMMARY = "ALPR Model for SOM"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "1.pl2020_2_2"

MODEL_DIR = "opt"

SRC_URI = " \
	file://${MODEL_DIR} \
"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

do_install() {
	cp -r ${WORKDIR}/opt ${D}/
	# Due to the way things are copied, we need to
	# potentially correct permissions
	#
	# We first have to clear all set-id perms (chmod won't clear these)
	chmod ug-s -R ${D}/*

	if [ -d ${D}/${LICENSE_FILES_DIRECTORY} ]; then
		# Now make sure the directory is set to 0755
		chmod 0755 ${D}/${LICENSE_FILES_DIRECTORY}
	fi
}

FILES_${PN} += " \
        /opt/* \
"
