# 
# Copyright 2019 Xilinx Inc.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#!/usr/bin/env bash
# Short-Description: Resize the root filesystem to fill partition

ROOT_PART=$(mount | sed -n 's|^\(.*\) on / .*|\1|p')
ROOT_DEV=$(echo $ROOT_PART | cut -c 1-12)
PART_NUM=$(echo -n $ROOT_PART | tail -c 1)
MOUNTED=$(mount | sed -n '/^\(.*\)'$PART_NUM' on \/[a-zA-Z0-9].*ext/p')

do_expand_rootfs() 
{
	local target=$1
	local part_num=$2

	if [ -n "${MOUNTED}" ]; then
		parted ${target} resizepart ${part_num} yes 100% > /dev/null 2>&1
	else
		parted ${target} resizepart ${part_num} 100% > /dev/null 2>&1
	fi
}

ext4_resize() 
{
	if [ "$ROOT_DEV" != "/dev/mmcblk1" -a "$ROOT_DEV" != "/dev/mmcblk0" ]; then
		echo "$ROOT_DEV is not an emmc or sd card. Don't know how to expand"
		exit
	fi

	do_expand_rootfs $ROOT_DEV $PART_NUM && resize2fs $ROOT_PART > /dev/null 2>&1
}

ext4_resize

