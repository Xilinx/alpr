
if [ $# -ne 1 ]; then
     echo "usage : gst-sanity.sh <path to mp4 file>"
     exit 1
fi

sudo TVM_THREAD_POOL_SPIN_COUNT=0 TVM_EXCLUDE_WORKER0=0 PYTHONPATH=/opt/uncanny/lib/python3.7/site-packages LD_LIBRARY_PATH=/opt/uncanny/lib GST_PLUGIN_PATH=/opt/uncanny/lib/gstreamer-1.0 gst-launch-1.0 -v -e filesrc location=$1 ! qtdemux ! h264parse ! omxh264dec internal-entropy-buffers=3 ! queue ! tee name=t t.src_0 ! queue ! ivas_xabrscaler xclbin-loc="/usr/lib/dpu.xclbin" ! video/x-raw, width=320, height=320, format=RGB ! queue ! ivas_xfilter kernels-config="/opt/uncanny/share/vitis_ai_library/models/alpr_vehicle/kernel_alpr_vehicle.json" ! queue ! scalem.sink_master ivas_xmetaaffixer name=scalem scalem.src_master ! queue ! fakesink t.src_1 ! queue ! scalem.sink_slave_0 scalem.src_slave_0 ! queue ! fpsdisplaysink video-sink="fakevideosink" text-overlay=false sync=true
