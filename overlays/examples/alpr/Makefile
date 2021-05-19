# (C) Copyright 2020 - 2021 Xilinx, Inc.
# SPDX-License-Identifier: Apache-2.0

DIR_PRJ = $(shell pwd)
RM = rm -rf
VIVADO_ROOT := $(XILINX_VIVADO)
VIVADO:=${VIVADO_ROOT}/bin/vivado

# Kernel name
KERNEL = v_multi_scaler
KERNEL_XO = $(KERNEL)/$(KERNEL).xo
TARGET = hw
#---------------------------------------------------------------------
# vitis common setup
.PHONY: help
help::
	@echo ""
	@echo "Makefile Usage:"
	@echo ""
	@echo "make all PLATFORM=<FPGA platform>"
	@echo "Command to generate the xclbin and bit"
	@echo ""
	@echo "make clean"
	@echo "Command to remove all the generated files"
	@echo ""

# # v++ flags
VPP ?= v++

XOCC_OPTS = -t ${TARGET} --platform ${PLATFORM} --save-temps --config ${DIR_PRJ}/prj_conf/prj_config_1dpu --xp param:compiler.userPostSysLinkOverlayTcl=${DIR_PRJ}/prj_conf/strip_interconnects.tcl --vivado.param board.repoPaths=${DIR_PRJ}/../../../platforms/vivado/board_files

dpu_HDLSRCS=kernel_xml/dpu/kernel.xml\
	     scripts/package_dpu_kernel.tcl\
	     scripts/gen_dpu_xo.tcl\
	     ../../dpu_ip/Vitis/dpu/hdl/DPUCZDX8G.v\
	     ../../dpu_ip/Vitis/dpu/inc/arch_def.vh\
	     ../../dpu_ip/Vitis/dpu/xdc/*.xdc\
	     ../../dpu_ip/DPUCZDX8G_*/hdl/DPUCZDX8G_*_dpu.sv\
	     ../../dpu_ip/DPUCZDX8G_*/inc/function.vh\
             ../../dpu_ip/DPUCZDX8G_*/inc/arch_para.vh

dpu_TCL=scripts/gen_dpu_xo.tcl
DPU_KERN_NAME = DPUCZDX8G
dpu_xo = binary_container_1/dpu.xo

KERNEL_XO_FLAGS = --kernel $(KERNEL) -I. -I./multiscaler/hls
JOBS = 32
VPP_XO_FLAGS = -t hw --platform $(PLATFORM) \
	-s -g $(KERNEL_XO_FLAGS)

dpu_ms_xo = $(KERNEL_XO) $(dpu_xo) 

.PHONY: all clean package

all : binary_container_1/dpu.xclbin package

# 	# Rules
$(KERNEL_XO): multiscaler/v_multi_scaler.cpp multiscaler/v_hresampler.cpp multiscaler/v_hscaler.cpp multiscaler/v_dma.cpp multiscaler/v_csc.cpp multiscaler/v_vresampler.cpp multiscaler/v_vscaler.cpp
	@mkdir -p $(@D)
	-@$(RM) $@
	$(VPP) $(VPP_XO_FLAGS) -c -o $@ $^
	-@$(RM) .Xil

binary_container_1/dpu.xo: $(dpu_HDLSRCS)
	@mkdir -p $(@D)
	-@$(RM) $@
	$(VIVADO) -mode batch -source $(dpu_TCL) -tclargs $@ $(DPU_KERN_NAME) ${TARGET} mpsoc

binary_container_1/dpu.xclbin: $(dpu_ms_xo)
	$(VPP) $(XOCC_OPTS) -l --temp_dir binary_container_1 --log_dir binary_container_1/logs --remote_ip_cache binary_container_1/ip_cache -o "$@" $(+)

package:
	-@mkdir -p binary_container_1/sd_card
	cp ./binary_*/link/vivado/vpl/prj/prj*/sources_1/bd/*/hw_handoff/*.hwh ./binary_*/sd_card
	cp ./binary_*/link/vivado/vpl/prj/prj.gen/sources_1/bd/*/ip/*_DPUCZDX8G_1_0/arch.json ./binary_*/sd_card
	cp ./binary_*/link/vivado/vpl/prj/prj.runs/impl_1/*.bit ./binary_*/sd_card
	cp ./binary_*/*.xclbin ./binary_*/sd_card

#.PHONY: clean
clean:
	-$(RM) $(KERNEL) *.log _x *.jou v++* *.xclbin *.ini *.xsa
	-$(RM) binary_container_1
	-$(RM) DPU_PP packaged* tmp_*


