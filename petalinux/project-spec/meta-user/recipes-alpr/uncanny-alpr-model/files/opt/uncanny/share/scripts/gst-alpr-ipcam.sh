
arr=( "$@" )

if [ $# -le 1 ]; then
     echo "usage : gst-alpr-ipcam.sh <num_streams> <stream0_path> <stream1_path> ..."
     exit 1
fi
if [ $1 -gt 2 ]; then
     echo "Only 2 instances are supported"
     exit 1
fi
pipe=" "
for (( c=0; c<$1; c++ ))
do
	loc_start_idx=1
	inst_idx=$(( $c + $loc_start_idx ))
	rtsp_loc=${arr[$inst_idx]}
	tee1_name="tee1_"$c
	tee2_name="tee2_"$c
	sc_name="sc_"$c
	ma_name="ma_"$c
	zmq_port=$((($c * 50) + 5022))
	zmq_loc="tcp://127.0.0.1:"$zmq_port
	
	pipe="rtspsrc latency=200 location=$rtsp_loc ! rtph264depay ! h264parse ! omxh264dec internal-entropy-buffers=3 ! queue ! tee name=$tee1_name $tee1_name.src_0 ! queue ! ivas_xabrscaler xclbin-loc=\"/usr/lib/dpu.xclbin\" ! video/x-raw, width=320, height=320, format=RGB ! queue ! ivas_xfilter kernels-config=\"/opt/uncanny/share/vitis_ai_library/models/alpr_vehicle/kernel_alpr_vehicle.json\" ! queue ! $ma_name.sink_master ivas_xmetaaffixer name=$ma_name $ma_name.src_master ! queue ! fakesink $tee1_name.src_1 ! queue ! $ma_name.sink_slave_0 $ma_name.src_slave_0 ! queue ! fpsdisplaysink video-sink=\"ivas_xzmqsink send-meta=1 endpoint=$zmq_loc pattern=1\" text-overlay=false sync=true "$pipe
done
pipe="sudo TVM_THREAD_POOL_SPIN_COUNT=0 TVM_EXCLUDE_WORKER0=0 PYTHONPATH=/opt/uncanny/lib/python3.7/site-packages LD_LIBRARY_PATH=/opt/uncanny/lib GST_PLUGIN_PATH=/opt/uncanny/lib/gstreamer-1.0 gst-launch-1.0 -v -e "$pipe
$pipe
