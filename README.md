# ALPR

To clone this repo :

git clone https://github.com/Xilinx/alpr.git


## Building Overlay project:

Step 1 : git clone --recursive https://github.com/Xilinx/kv260-vitis.git and build the kv260_ispMipiRx_vcu_DP platform 
Step 2 : Copy the alpr overlay project to kv260-vitis working directory
```
	cp -rf overlays/examples/alpr $kv260_vitis_working_dir/overlays/examples/
```
Step 3 : Add alpr to OVERLAY_LIST in $kv260_vitis_working_dir/Makefile
```
	OVERLAY_LIST = smartcam aibox-reid defect-detect nlp-smartvision alpr
```
Step 4 : Compile the overlay project
```
	make overlay PFM=kv260_ispMipiRx_vcu_DP OVERLAY=alpr
```

Firmware files are generated at below path
```
	$kv260_vitis_working_dir/overlays/examples/alpr/binary_container_1/link/int/system.bit
	$kv260_vitis_working_dir/overlays/examples/alpr/binary_container_1/dpu.xclbin
```

## Build Petalinux project:

Follow below steps to build Petalinux project with ALPR recipes. 
We assume that the base petalinux project for SOM is already created. Let's call petalinux working directory as $plnx_working_dir

Step 1 : Copy petalinux/project-spec/meta-user/recipes-alpr and petalinux/project-spec/meta-user/recipes-core directories to $plnx_working_dir/project-spec/meta-user/
Step 2 : Enable Application packagegroup
```
	echo 'BOARD_VARIANT = "kv"' >> project-spec/meta-user/conf/petalinuxbsp.conf
	echo 'CONFIG_uncanny-packagegroup-kv260-alpr' >> project-spec/meta-user/conf/user-rootfsconfig
	petalinux-config -c rootfs
```
Step 3 : In petalinux-config menu, select user packages ---> , then toggle on the newly added application packagegroups by typing y next to those entries. Then save and exit the configuration.
Step 4 : Build Petalinux
```
	petalinux-build
```
Step 5 : Package files to create SD card image
```
	petalinux-package --wic --bootfiles "ramdisk.cpio.gz.u-boot boot.scr Image system.dtb"
```
RPM files can be found under <PLNX_TMP_DIR>/deploy/rpm

