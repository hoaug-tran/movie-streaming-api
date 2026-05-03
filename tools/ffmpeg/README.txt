FFmpeg 64-bit static Windows build from www.gyan.dev

Version: 2026-02-02-git-7e9fe341df-full_build-www.gyan.dev

License: GPL v3

Source Code: https://github.com/FFmpeg/FFmpeg/commit/7e9fe341df

External Assets
frei0r plugins:   https://www.gyan.dev/ffmpeg/builds/ffmpeg-frei0r-plugins
lensfun database: https://www.gyan.dev/ffmpeg/builds/ffmpeg-lensfun-db
whisper models:   https://huggingface.co/ggerganov/whisper.cpp/tree/main

git-full build configuration: 

ARCH                      x86 (generic)
big-endian                no
runtime cpu detection     yes
standalone assembly       yes
x86 assembler             nasm
MMX enabled               yes
MMXEXT enabled            yes
SSE enabled               yes
SSSE3 enabled             yes
AESNI enabled             yes
CLMUL enabled             yes
AVX enabled               yes
AVX2 enabled              yes
AVX-512 enabled           yes
AVX-512ICL enabled        yes
XOP enabled               yes
FMA3 enabled              yes
FMA4 enabled              yes
i686 features enabled     yes
CMOV is fast              yes
EBX available             yes
EBP available             yes
debug symbols             yes
strip symbols             yes
optimize for size         no
optimizations             yes
static                    yes
shared                    no
network support           yes
threading support         pthreads
safe bitstream reader     yes
texi2html enabled         no
perl enabled              yes
pod2man enabled           yes
makeinfo enabled          yes
makeinfo supports HTML    yes
experimental features     yes
xmllint enabled           yes

External libraries:
avisynth                libgsm                  libsvtav1
bzlib                   libharfbuzz             libsvtjpegxs
cairo                   libilbc                 libtheora
chromaprint             libjxl                  libtwolame
frei0r                  liblc3                  libuavs3d
gmp                     liblensfun              libvidstab
gnutls                  libmodplug              libvmaf
iconv                   libmp3lame              libvo_amrwbenc
ladspa                  libmysofa               libvorbis
lcms2                   liboapv                 libvpx
libaom                  libopencore_amrnb       libvvenc
libaribb24              libopencore_amrwb       libwebp
libaribcaption          libopenjpeg             libx264
libass                  libopenmpt              libx265
libbluray               libopus                 libxavs2
libbs2b                 libplacebo              libxevd
libcaca                 libqrencode             libxeve
libcdio                 libquirc                libxml2
libcodec2               librav1e                libxvid
libdav1d                librist                 libzimg
libdavs2                librubberband           libzmq
libdvdnav               libshaderc              libzvbi
libdvdread              libshine                lzma
libflite                libsnappy               mediafoundation
libfontconfig           libsoxr                 openal
libfreetype             libspeex                sdl2
libfribidi              libsrt                  whisper
libgme                  libssh                  zlib

External libraries providing hardware acceleration:
amf                     d3d12va                 nvdec
cuda                    dxva2                   nvenc
cuda_llvm               ffnvcodec               opencl
cuvid                   libmfx                  vaapi
d3d11va                 libvpl                  vulkan

Libraries:
avcodec                 avformat                swscale
avdevice                avutil
avfilter                swresample

Programs:
ffmpeg                  ffplay                  ffprobe

Enabled decoders:
aac                     fmvc                    pcm_u32be
aac_fixed               fourxm                  pcm_u32le
aac_latm                fraps                   pcm_u8
aasc                    frwu                    pcm_vidc
ac3                     ftr                     pcx
ac3_fixed               g2m                     pdv
acelp_kelvin            g723_1                  pfm
adpcm_4xm               g728                    pgm
adpcm_adx               g729                    pgmyuv
adpcm_afc               gdv                     pgssub
adpcm_agm               gem                     pgx
adpcm_aica              gif                     phm
adpcm_argo              gremlin_dpcm            photocd
adpcm_circus            gsm                     pictor
adpcm_ct                gsm_ms                  pixlet
adpcm_dtk               h261                    pjs
adpcm_ea                h263                    png
adpcm_ea_maxis_xa       h263i                   ppm
adpcm_ea_r1             h263p                   prores
adpcm_ea_r2             h264                    prores_raw
adpcm_ea_r3             h264_amf                prosumer
adpcm_ea_xas            h264_cuvid              psd
adpcm_g722              h264_qsv                ptx
adpcm_g726              hap                     qcelp
adpcm_g726le            hca                     qdm2
adpcm_ima_acorn         hcom                    qdmc
adpcm_ima_alp           hdr                     qdraw
adpcm_ima_amv           hevc                    qoa
adpcm_ima_apc           hevc_amf                qoi
adpcm_ima_apm           hevc_cuvid              qpeg
adpcm_ima_cunning       hevc_qsv                qtrle
adpcm_ima_dat4          hnm4_video              r10k
adpcm_ima_dk3           hq_hqa                  r210
adpcm_ima_dk4           hqx                     ra_144
adpcm_ima_ea_eacs       huffyuv                 ra_288
adpcm_ima_ea_sead       hymt                    ralf
adpcm_ima_escape        iac                     rasc
adpcm_ima_hvqm2         idcin                   rawvideo
adpcm_ima_hvqm4         idf                     realtext
adpcm_ima_iss           iff_ilbm                rka
adpcm_ima_magix         ilbc                    rl2
adpcm_ima_moflex        imc                     roq
adpcm_ima_mtf           imm4                    roq_dpcm
adpcm_ima_oki           imm5                    rpza
adpcm_ima_pda           indeo2                  rscc
adpcm_ima_qt            indeo3                  rtv1
adpcm_ima_rad           indeo4                  rv10
adpcm_ima_smjpeg        indeo5                  rv20
adpcm_ima_ssi           interplay_acm           rv30
adpcm_ima_wav           interplay_dpcm          rv40
adpcm_ima_ws            interplay_video         rv60
adpcm_ima_xbox          ipu                     s302m
adpcm_ms                jacosub                 sami
adpcm_mtaf              jpeg2000                sanm
adpcm_n64               jpegls                  sbc
adpcm_psx               jv                      scpr
adpcm_psxc              kgv1                    screenpresso
adpcm_sanyo             kmvc                    sdx2_dpcm
adpcm_sbpro_2           lagarith                sga
adpcm_sbpro_3           lead                    sgi
adpcm_sbpro_4           libaom_av1              sgirle
adpcm_swf               libaribb24              sheervideo
adpcm_thp               libaribcaption          shorten
adpcm_thp_le            libcodec2               simbiosis_imx
adpcm_vima              libdav1d                sipr
adpcm_xa                libdavs2                siren
adpcm_xmd               libgsm                  smackaud
adpcm_yamaha            libgsm_ms               smacker
adpcm_zork              libilbc                 smc
agm                     libjxl                  smvjpeg
ahx                     libjxl_anim             snow
aic                     liblc3                  sol_dpcm
alac                    libopencore_amrnb       sonic
alias_pix               libopencore_amrwb       sp5x
als                     libopus                 speedhq
amrnb                   libspeex                speex
amrwb                   libsvtjpegxs            srgc
amv                     libuavs3d               srt
anm                     libvorbis               ssa
ansi                    libvpx_vp8              stl
anull                   libvpx_vp9              subrip
apac                    libxevd                 subviewer
ape                     libzvbi_teletext        subviewer1
apng                    loco                    sunrast
aptx                    lscr                    svq1
aptx_hd                 m101                    svq3
apv                     mace3                   tak
arbc                    mace6                   targa
argo                    magicyuv                targa_y216
ass                     mdec                    tdsc
asv1                    media100                text
asv2                    metasound               theora
atrac1                  microdvd                thp
atrac3                  mimic                   tiertexseqvideo
atrac3al                misc4                   tiff
atrac3p                 mjpeg                   tmv
atrac3pal               mjpeg_cuvid             truehd
atrac9                  mjpeg_qsv               truemotion1
aura                    mjpegb                  truemotion2
aura2                   mlp                     truemotion2rt
av1                     mmvideo                 truespeech
av1_amf                 mobiclip                tscc
av1_cuvid               motionpixels            tscc2
av1_qsv                 movtext                 tta
avrn                    mp1                     twinvq
avrp                    mp1float                txd
avs                     mp2                     ulti
avui                    mp2float                utvideo
bethsoftvid             mp3                     v210
bfi                     mp3adu                  v210x
bink                    mp3adufloat             v308
binkaudio_dct           mp3float                v408
binkaudio_rdft          mp3on4                  v410
bintext                 mp3on4float             vb
bitpacked               mpc7                    vble
bmp                     mpc8                    vbn
bmv_audio               mpeg1_cuvid             vc1
bmv_video               mpeg1video              vc1_cuvid
bonk                    mpeg2_cuvid             vc1_qsv
brender_pix             mpeg2_qsv               vc1image
c93                     mpeg2video              vcr1
cavs                    mpeg4                   vmdaudio
cbd2_dpcm               mpeg4_cuvid             vmdvideo
ccaption                mpegvideo               vmix
cdgraphics              mpl2                    vmnc
cdtoons                 msa1                    vnull
cdxl                    mscc                    vorbis
cfhd                    msmpeg4v1               vp3
cinepak                 msmpeg4v2               vp4
clearvideo              msmpeg4v3               vp5
cljr                    msnsiren                vp6
cllc                    msp2                    vp6a
comfortnoise            msrle                   vp6f
cook                    mss1                    vp7
cpia                    mss2                    vp8
cri                     msvideo1                vp8_cuvid
cscd                    mszh                    vp8_qsv
cyuv                    mts2                    vp9
dca                     mv30                    vp9_amf
dds                     mvc1                    vp9_cuvid
derf_dpcm               mvc2                    vp9_qsv
dfa                     mvdv                    vplayer
dfpwm                   mvha                    vqa
dirac                   mwsc                    vqc
dnxhd                   mxpeg                   vvc
dolby_e                 nellymoser              vvc_qsv
dpx                     notchlc                 wady_dpcm
dsd_lsbf                nuv                     wavarc
dsd_lsbf_planar         on2avc                  wavpack
dsd_msbf                opus                    wbmp
dsd_msbf_planar         osq                     wcmv
dsicinaudio             paf_audio               webp
dsicinvideo             paf_video               webvtt
dss_sp                  pam                     wmalossless
dst                     pbm                     wmapro
dvaudio                 pcm_alaw                wmav1
dvbsub                  pcm_bluray              wmav2
dvdsub                  pcm_dvd                 wmavoice
dvvideo                 pcm_f16le               wmv1
dxa                     pcm_f24le               wmv2
dxtory                  pcm_f32be               wmv3
dxv                     pcm_f32le               wmv3image
eac3                    pcm_f64be               wnv1
eacmv                   pcm_f64le               wrapped_avframe
eamad                   pcm_lxf                 ws_snd1
eatgq                   pcm_mulaw               xan_dpcm
eatgv                   pcm_s16be               xan_wc3
eatqi                   pcm_s16be_planar        xan_wc4
eightbps                pcm_s16le               xbin
eightsvx_exp            pcm_s16le_planar        xbm
eightsvx_fib            pcm_s24be               xface
escape124               pcm_s24daud             xl
escape130               pcm_s24le               xma1
evrc                    pcm_s24le_planar        xma2
exr                     pcm_s32be               xpm
fastaudio               pcm_s32le               xsub
ffv1                    pcm_s32le_planar        xwd
ffvhuff                 pcm_s64be               y41p
ffwavesynth             pcm_s64le               ylc
fic                     pcm_s8                  yop
fits                    pcm_s8_planar           yuv4
flac                    pcm_sga                 zero12v
flashsv                 pcm_u16be               zerocodec
flashsv2                pcm_u16le               zlib
flic                    pcm_u24be               zmbv
flv                     pcm_u24le

Enabled encoders:
a64multi                hevc_mf                 pcm_s32le
a64multi5               hevc_nvenc              pcm_s32le_planar
aac                     hevc_qsv                pcm_s64be
aac_mf                  hevc_vaapi              pcm_s64le
ac3                     hevc_vulkan             pcm_s8
ac3_fixed               huffyuv                 pcm_s8_planar
ac3_mf                  jpeg2000                pcm_u16be
adpcm_adx               jpegls                  pcm_u16le
adpcm_argo              libaom_av1              pcm_u24be
adpcm_g722              libcodec2               pcm_u24le
adpcm_g726              libgsm                  pcm_u32be
adpcm_g726le            libgsm_ms               pcm_u32le
adpcm_ima_alp           libilbc                 pcm_u8
adpcm_ima_amv           libjxl                  pcm_vidc
adpcm_ima_apm           libjxl_anim             pcx
adpcm_ima_qt            liblc3                  pfm
adpcm_ima_ssi           libmp3lame              pgm
adpcm_ima_wav           liboapv                 pgmyuv
adpcm_ima_ws            libopencore_amrnb       phm
adpcm_ms                libopenjpeg             png
adpcm_swf               libopus                 ppm
adpcm_yamaha            librav1e                prores
alac                    libshine                prores_aw
alias_pix               libspeex                prores_ks
amv                     libsvtav1               qoi
anull                   libsvtjpegxs            qtrle
apng                    libtheora               r10k
aptx                    libtwolame              r210
aptx_hd                 libvo_amrwbenc          ra_144
ass                     libvorbis               rawvideo
asv1                    libvpx_vp8              roq
asv2                    libvpx_vp9              roq_dpcm
av1_amf                 libvvenc                rpza
av1_d3d12va             libwebp                 rv10
av1_mf                  libwebp_anim            rv20
av1_nvenc               libx264                 s302m
av1_qsv                 libx264rgb              sbc
av1_vaapi               libx265                 sgi
av1_vulkan              libxavs2                smc
avrp                    libxeve                 snow
avui                    libxvid                 speedhq
bitpacked               ljpeg                   srt
bmp                     magicyuv                ssa
cfhd                    mjpeg                   subrip
cinepak                 mjpeg_qsv               sunrast
cljr                    mjpeg_vaapi             svq1
comfortnoise            mlp                     targa
dca                     movtext                 text
dfpwm                   mp2                     tiff
dnxhd                   mp2fixed                truehd
dpx                     mp3_mf                  tta
dvbsub                  mpeg1video              ttml
dvdsub                  mpeg2_qsv               utvideo
dvvideo                 mpeg2_vaapi             v210
dxv                     mpeg2video              v308
eac3                    mpeg4                   v408
exr                     msmpeg4v2               v410
ffv1                    msmpeg4v3               vbn
ffv1_vulkan             msrle                   vc2
ffvhuff                 msvideo1                vnull
fits                    nellymoser              vorbis
flac                    opus                    vp8_vaapi
flashsv                 pam                     vp9_qsv
flashsv2                pbm                     vp9_vaapi
flv                     pcm_alaw                wavpack
g723_1                  pcm_bluray              wbmp
gif                     pcm_dvd                 webvtt
h261                    pcm_f32be               wmav1
h263                    pcm_f32le               wmav2
h263p                   pcm_f64be               wmv1
h264_amf                pcm_f64le               wmv2
h264_d3d12va            pcm_mulaw               wrapped_avframe
h264_mf                 pcm_s16be               xbm
h264_nvenc              pcm_s16be_planar        xface
h264_qsv                pcm_s16le               xsub
h264_vaapi              pcm_s16le_planar        xwd
h264_vulkan             pcm_s24be               y41p
hap                     pcm_s24daud             yuv4
hdr                     pcm_s24le               zlib
hevc_amf                pcm_s24le_planar        zmbv
hevc_d3d12va            pcm_s32be

Enabled hwaccels:
av1_d3d11va             hevc_dxva2              vc1_dxva2
av1_d3d11va2            hevc_nvdec              vc1_nvdec
av1_d3d12va             hevc_vaapi              vc1_vaapi
av1_dxva2               hevc_vulkan             vp8_nvdec
av1_nvdec               mjpeg_nvdec             vp8_vaapi
av1_vaapi               mjpeg_vaapi             vp9_d3d11va
av1_vulkan              mpeg1_nvdec             vp9_d3d11va2
dpx_vulkan              mpeg2_d3d11va           vp9_d3d12va
ffv1_vulkan             mpeg2_d3d11va2          vp9_dxva2
h263_vaapi              mpeg2_d3d12va           vp9_nvdec
h264_d3d11va            mpeg2_dxva2             vp9_vaapi
h264_d3d11va2           mpeg2_nvdec             vp9_vulkan
h264_d3d12va            mpeg2_vaapi             vvc_vaapi
h264_dxva2              mpeg4_nvdec             wmv3_d3d11va
h264_nvdec              mpeg4_vaapi             wmv3_d3d11va2
h264_vaapi              prores_raw_vulkan       wmv3_d3d12va
h264_vulkan             prores_vulkan           wmv3_dxva2
hevc_d3d11va            vc1_d3d11va             wmv3_nvdec
hevc_d3d11va2           vc1_d3d11va2            wmv3_vaapi
hevc_d3d12va            vc1_d3d12va

Enabled parsers:
aac                     dvdsub                  mpegvideo
aac_latm                evc                     opus
ac3                     ffv1                    png
adx                     flac                    pnm
ahx                     ftr                     prores
amr                     g723_1                  prores_raw
apv                     g729                    qoi
av1                     gif                     rv34
avs2                    gsm                     sbc
avs3                    h261                    sipr
bmp                     h263                    tak
cavsvideo               h264                    vc1
cook                    hdr                     vorbis
cri                     hevc                    vp3
dca                     ipu                     vp8
dirac                   jpeg2000                vp9
dnxhd                   jpegxl                  vvc
dnxuc                   jpegxs                  webp
dolby_e                 misc4                   xbm
dpx                     mjpeg                   xma
dvaudio                 mlp                     xwd
dvbsub                  mpeg4video
dvd_nav                 mpegaudio

Enabled demuxers:
aa                      ico                     pcm_f64be
aac                     idcin                   pcm_f64le
aax                     idf                     pcm_mulaw
ac3                     iff                     pcm_s16be
ac4                     ifv                     pcm_s16le
ace                     ilbc                    pcm_s24be
acm                     image2                  pcm_s24le
act                     image2_alias_pix        pcm_s32be
adf                     image2_brender_pix      pcm_s32le
adp                     image2pipe              pcm_s8
ads                     image_bmp_pipe          pcm_u16be
adx                     image_cri_pipe          pcm_u16le
aea                     image_dds_pipe          pcm_u24be
afc                     image_dpx_pipe          pcm_u24le
aiff                    image_exr_pipe          pcm_u32be
aix                     image_gem_pipe          pcm_u32le
alp                     image_gif_pipe          pcm_u8
amr                     image_hdr_pipe          pcm_vidc
amrnb                   image_j2k_pipe          pdv
amrwb                   image_jpeg_pipe         pjs
anm                     image_jpegls_pipe       pmp
apac                    image_jpegxl_pipe       pp_bnk
apc                     image_jpegxs_pipe       pva
ape                     image_pam_pipe          pvf
apm                     image_pbm_pipe          qcp
apng                    image_pcx_pipe          qoa
aptx                    image_pfm_pipe          r3d
aptx_hd                 image_pgm_pipe          rawvideo
apv                     image_pgmyuv_pipe       rcwt
aqtitle                 image_pgx_pipe          realtext
argo_asf                image_phm_pipe          redspark
argo_brp                image_photocd_pipe      rka
argo_cvg                image_pictor_pipe       rl2
asf                     image_png_pipe          rm
asf_o                   image_ppm_pipe          roq
ass                     image_psd_pipe          rpl
ast                     image_qdraw_pipe        rsd
au                      image_qoi_pipe          rso
av1                     image_sgi_pipe          rtp
avi                     image_sunrast_pipe      rtsp
avisynth                image_svg_pipe          s337m
avr                     image_tiff_pipe         sami
avs                     image_vbn_pipe          sap
avs2                    image_webp_pipe         sbc
avs3                    image_xbm_pipe          sbg
bethsoftvid             image_xpm_pipe          scc
bfi                     image_xwd_pipe          scd
bfstm                   imf                     sdns
bink                    ingenient               sdp
binka                   ipmovie                 sdr2
bintext                 ipu                     sds
bit                     ircam                   sdx
bitpacked               iss                     segafilm
bmv                     iv8                     ser
boa                     ivf                     sga
bonk                    ivr                     shorten
brstm                   jacosub                 siff
c93                     jpegxl_anim             simbiosis_imx
caf                     jv                      sln
cavsvideo               kux                     smacker
cdg                     kvag                    smjpeg
cdxl                    laf                     smush
cine                    lc3                     sol
codec2                  libgme                  sox
codec2raw               libmodplug              spdif
concat                  libopenmpt              srt
dash                    live_flv                stl
data                    lmlm4                   str
daud                    loas                    subviewer
dcstr                   lrc                     subviewer1
derf                    luodat                  sup
dfa                     lvf                     svag
dfpwm                   lxf                     svs
dhav                    m4v                     swf
dirac                   matroska                tak
dnxhd                   mca                     tedcaptions
dsf                     mcc                     thp
dsicin                  mgsts                   threedostr
dss                     microdvd                tiertexseq
dts                     mjpeg                   tmv
dtshd                   mjpeg_2000              truehd
dv                      mlp                     tta
dvbsub                  mlv                     tty
dvbtxt                  mm                      txd
dvdvideo                mmf                     ty
dxa                     mods                    usm
ea                      moflex                  v210
ea_cdata                mov                     v210x
eac3                    mp3                     vag
epaf                    mpc                     vc1
evc                     mpc8                    vc1t
ffmetadata              mpegps                  vividas
filmstrip               mpegts                  vivo
fits                    mpegtsraw               vmd
flac                    mpegvideo               vobsub
flic                    mpjpeg                  voc
flv                     mpl2                    vpk
fourxm                  mpsub                   vplayer
frm                     msf                     vqf
fsb                     msnwc_tcp               vvc
fwse                    msp                     w64
g722                    mtaf                    wady
g723_1                  mtv                     wav
g726                    musx                    wavarc
g726le                  mv                      wc3
g728                    mvi                     webm_dash_manifest
g729                    mxf                     webvtt
gdv                     mxg                     wsaud
genh                    nc                      wsd
gif                     nistsphere              wsvqa
gsm                     nsp                     wtv
gxf                     nsv                     wv
h261                    nut                     wve
h263                    nuv                     xa
h264                    obu                     xbin
hca                     ogg                     xmd
hcom                    oma                     xmv
hevc                    osq                     xvag
hls                     paf                     xwma
hnm                     pcm_alaw                yop
hxvs                    pcm_f32be               yuv4mpegpipe
iamf                    pcm_f32le

Enabled muxers:
a64                     h263                    pcm_s24be
ac3                     h264                    pcm_s24le
ac4                     hash                    pcm_s32be
adts                    hds                     pcm_s32le
adx                     hevc                    pcm_s8
aea                     hls                     pcm_u16be
aiff                    iamf                    pcm_u16le
alp                     ico                     pcm_u24be
amr                     ilbc                    pcm_u24le
amv                     image2                  pcm_u32be
apm                     image2pipe              pcm_u32le
apng                    ipod                    pcm_u8
aptx                    ircam                   pcm_vidc
aptx_hd                 ismv                    psp
apv                     ivf                     rawvideo
argo_asf                jacosub                 rcwt
argo_cvg                kvag                    rm
asf                     latm                    roq
asf_stream              lc3                     rso
ass                     lrc                     rtp
ast                     m4v                     rtp_mpegts
au                      matroska                rtsp
avi                     matroska_audio          sap
avif                    mcc                     sbc
avm2                    md5                     scc
avs2                    microdvd                segafilm
avs3                    mjpeg                   segment
bit                     mkvtimestamp_v2         smjpeg
caf                     mlp                     smoothstreaming
cavsvideo               mmf                     sox
chromaprint             mov                     spdif
codec2                  mp2                     spx
codec2raw               mp3                     srt
crc                     mp4                     stream_segment
dash                    mpeg1system             streamhash
data                    mpeg1vcd                sup
daud                    mpeg1video              swf
dfpwm                   mpeg2dvd                tee
dirac                   mpeg2svcd               tg2
dnxhd                   mpeg2video              tgp
dts                     mpeg2vob                truehd
dv                      mpegts                  tta
eac3                    mpjpeg                  ttml
evc                     mxf                     uncodedframecrc
f4v                     mxf_d10                 vc1
ffmetadata              mxf_opatom              vc1t
fifo                    null                    voc
filmstrip               nut                     vvc
fits                    obu                     w64
flac                    oga                     wav
flv                     ogg                     webm
framecrc                ogv                     webm_chunk
framehash               oma                     webm_dash_manifest
framemd5                opus                    webp
g722                    pcm_alaw                webvtt
g723_1                  pcm_f32be               whip
g726                    pcm_f32le               wsaud
g726le                  pcm_f64be               wtv
gif                     pcm_f64le               wv
gsm                     pcm_mulaw               yuv4mpegpipe
gxf                     pcm_s16be
h261                    pcm_s16le

Enabled protocols:
async                   hls                     prompeg
bluray                  http                    rtmp
cache                   httpproxy               rtmpe
concat                  https                   rtmps
concatf                 icecast                 rtmpt
crypto                  ipfs_gateway            rtmpte
data                    ipns_gateway            rtmpts
dtls                    librist                 rtp
fd                      libsrt                  srtp
ffrtmpcrypt             libssh                  subfile
ffrtmphttp              libzmq                  tcp
file                    md5                     tee
ftp                     mmsh                    tls
gopher                  mmst                    udp
gophers                 pipe                    udplite

Enabled filters:
a3dscope                decimate                perms
aap                     deconvolve              perspective
abench                  dedot                   phase
abitscope               deesser                 photosensitivity
acompressor             deflate                 pixdesctest
acontrast               deflicker               pixelize
acopy                   deinterlace_qsv         pixscope
acrossfade              deinterlace_vaapi       pp7
acrossover              dejudder                premultiply
acrusher                delogo                  premultiply_dynamic
acue                    denoise_vaapi           prewitt
addroi                  deshake                 prewitt_opencl
adeclick                deshake_opencl          procamp_vaapi
adeclip                 despill                 program_opencl
adecorrelate            detelecine              pseudocolor
adelay                  dialoguenhance          psnr
adenorm                 dilation                pullup
aderivative             dilation_opencl         qp
adrawgraph              displace                qrencode
adrc                    doubleweave             qrencodesrc
adynamicequalizer       drawbox                 quirc
adynamicsmooth          drawbox_vaapi           random
aecho                   drawgraph               readeia608
aemphasis               drawgrid                readvitc
aeval                   drawtext                realtime
aevalsrc                drawvg                  remap
aexciter                drmeter                 remap_opencl
afade                   dynaudnorm              removegrain
afdelaysrc              earwax                  removelogo
afftdn                  ebur128                 repeatfields
afftfilt                edgedetect              replaygain
afir                    elbg                    reverse
afireqsrc               entropy                 rgbashift
afirsrc                 epx                     rgbtestsrc
aformat                 eq                      roberts
afreqshift              equalizer               roberts_opencl
afwtdn                  erosion                 rotate
agate                   erosion_opencl          rubberband
agraphmonitor           estdif                  sab
ahistogram              exposure                scale
aiir                    extractplanes           scale2ref
aintegral               extrastereo             scale_cuda
ainterleave             fade                    scale_d3d11
alatency                feedback                scale_d3d12
alimiter                fftdnoiz                scale_qsv
allpass                 fftfilt                 scale_vaapi
allrgb                  field                   scale_vulkan
allyuv                  fieldhint               scdet
aloop                   fieldmatch              scdet_vulkan
alphaextract            fieldorder              scharr
alphamerge              fillborders             scroll
amerge                  find_rect               segment
ametadata               firequalizer            select
amf_capture             flanger                 selectivecolor
amix                    flip_vulkan             sendcmd
amovie                  flite                   separatefields
amplify                 floodfill               setdar
amultiply               format                  setfield
anequalizer             fps                     setparams
anlmdn                  framepack               setpts
anlmf                   framerate               setrange
anlms                   framestep               setsar
anoisesrc               freezedetect            settb
anull                   freezeframes            sharpness_vaapi
anullsink               frei0r                  shear
anullsrc                frei0r_src              showcqt
apad                    fspp                    showcwt
aperms                  fsync                   showfreqs
aphasemeter             gblur                   showinfo
aphaser                 gblur_vulkan            showpalette
aphaseshift             geq                     showspatial
apsnr                   gfxcapture              showspectrum
apsyclip                gradfun                 showspectrumpic
apulsator               gradients               showvolume
arealtime               graphmonitor            showwaves
aresample               grayworld               showwavespic
areverse                greyedge                shuffleframes
arls                    guided                  shufflepixels
arnndn                  haas                    shuffleplanes
asdr                    haldclut                sidechaincompress
asegment                haldclutsrc             sidechaingate
aselect                 hdcd                    sidedata
asendcmd                headphone               sierpinski
asetnsamples            hflip                   signalstats
asetpts                 hflip_vulkan            signature
asetrate                highpass                silencedetect
asettb                  highshelf               silenceremove
ashowinfo               hilbert                 sinc
asidedata               histeq                  sine
asisdr                  histogram               siti
asoftclip               hqdn3d                  smartblur
aspectralstats          hqx                     smptebars
asplit                  hstack                  smptehdbars
ass                     hstack_qsv              sobel
astats                  hstack_vaapi            sobel_opencl
astreamselect           hsvhold                 sofalizer
asubboost               hsvkey                  spectrumsynth
asubcut                 hue                     speechnorm
asupercut               huesaturation           split
asuperpass              hwdownload              spp
asuperstop              hwmap                   sr_amf
atadenoise              hwupload                ssim
atempo                  hwupload_cuda           ssim360
atilt                   hysteresis              stereo3d
atrim                   iccdetect               stereotools
avectorscope            iccgen                  stereowiden
avgblur                 identity                streamselect
avgblur_opencl          idet                    subtitles
avgblur_vulkan          il                      super2xsai
avsynctest              inflate                 superequalizer
axcorrelate             interlace               surround
azmq                    interlace_vulkan        swaprect
backgroundkey           interleave              swapuv
bandpass                join                    tblend
bandreject              kerndeint               telecine
bass                    kirsch                  testsrc
bbox                    ladspa                  testsrc2
bench                   lagfun                  thistogram
bilateral               latency                 threshold
bilateral_cuda          lenscorrection          thumbnail
biquad                  lensfun                 thumbnail_cuda
bitplanenoise           libplacebo              tile
blackdetect             libvmaf                 tiltandshift
blackdetect_vulkan      life                    tiltshelf
blackframe              limitdiff               tinterlace
blend                   limiter                 tlut2
blend_vulkan            loop                    tmedian
blockdetect             loudnorm                tmidequalizer
blurdetect              lowpass                 tmix
bm3d                    lowshelf                tonemap
boxblur                 lumakey                 tonemap_opencl
boxblur_opencl          lut                     tonemap_vaapi
bs2b                    lut1d                   tpad
bwdif                   lut2                    transpose
bwdif_cuda              lut3d                   transpose_opencl
bwdif_vulkan            lutrgb                  transpose_vaapi
cas                     lutyuv                  transpose_vulkan
ccrepack                mandelbrot              treble
cellauto                maskedclamp             tremolo
channelmap              maskedmax               trim
channelsplit            maskedmerge             unpremultiply
chorus                  maskedmin               unsharp
chromaber_vulkan        maskedthreshold         unsharp_opencl
chromahold              maskfun                 untile
chromakey               mcdeint                 uspp
chromakey_cuda          mcompand                v360
chromanr                median                  vaguedenoiser
chromashift             mergeplanes             varblur
ciescope                mestimate               vectorscope
codecview               mestimate_d3d12         vflip
color                   metadata                vflip_vulkan
color_vulkan            midequalizer            vfrdet
colorbalance            minterpolate            vibrance
colorchannelmixer       mix                     vibrato
colorchart              monochrome              vidstabdetect
colorcontrast           morpho                  vidstabtransform
colorcorrect            movie                   vif
colordetect             mpdecimate              vignette
colorhold               mptestsrc               virtualbass
colorize                msad                    vmafmotion
colorkey                multiply                volume
colorkey_opencl         negate                  volumedetect
colorlevels             nlmeans                 vpp_amf
colormap                nlmeans_opencl          vpp_qsv
colormatrix             nlmeans_vulkan          vstack
colorspace              nnedi                   vstack_qsv
colorspace_cuda         noformat                vstack_vaapi
colorspectrum           noise                   w3fdif
colortemperature        normalize               waveform
compand                 null                    weave
compensationdelay       nullsink                whisper
concat                  nullsrc                 xbr
convolution             openclsrc               xcorrelate
convolution_opencl      oscilloscope            xfade
convolve                overlay                 xfade_opencl
copy                    overlay_cuda            xfade_vulkan
corr                    overlay_opencl          xmedian
cover_rect              overlay_qsv             xpsnr
crop                    overlay_vaapi           xstack
cropdetect              overlay_vulkan          xstack_qsv
crossfeed               owdenoise               xstack_vaapi
crystalizer             pad                     yadif
cue                     pad_cuda                yadif_cuda
curves                  pad_opencl              yaepblur
datascope               pad_vaapi               yuvtestsrc
dblur                   pal100bars              zmq
dcshift                 pal75bars               zoneplate
dctdnoiz                palettegen              zoompan
ddagrab                 paletteuse              zscale
deband                  pan
deblock                 perlin

Enabled bsfs:
aac_adtstoasc           h264_metadata           pgs_frame_merge
ahx_to_mp2              h264_mp4toannexb        prores_metadata
apv_metadata            h264_redundant_pps      remove_extradata
av1_frame_merge         hapqa_extract           setts
av1_frame_split         hevc_metadata           showinfo
av1_metadata            hevc_mp4toannexb        smpte436m_to_eia608
chomp                   imx_dump_header         text2movsub
dca_core                media100_to_mjpegb      trace_headers
dovi_rpu                mjpeg2jpeg              truehd_core
dts2pts                 mjpega_dump_header      vp9_metadata
dump_extradata          mov2textsub             vp9_raw_reorder
dv_error_marker         mpeg2_metadata          vp9_superframe
eac3_core               mpeg4_unpack_bframes    vp9_superframe_split
eia608_to_smpte436m     noise                   vvc_metadata
evc_frame_merge         null                    vvc_mp4toannexb
extract_extradata       opus_metadata
filter_units            pcm_rechunk

Enabled indevs:
dshow                   lavfi                   openal
gdigrab                 libcdio                 vfwcap

Enabled outdevs:
caca

git-full external libraries' versions: 

AMF v1.5.0-1-gd0b3e6d
aom v3.13.1-217-g29e0f9faea
aribcaption 1.1.1
AviSynthPlus v3.7.5-184-g777cae44
bs2b 3.1.0
cairo 1.18.5
chromaprint 1.6.0
codec2 1.2.0-106-g96e8a19c
dav1d 1.5.3-15-g60507bf
davs2 1.7-1-gb41cf11
dvdnav 7.0.0-1-gcf11277
dvdread 7.0.1-37-ge294cf7
ffnvcodec n13.0.19.0-2-g876af32
flite v2.2-55-g6c9f20d
frei0r v2.5.1-3-g74edaeb
gsm 1.0.23
ladspa-sdk 1.17
lame 3.100
lc3 1.1.3
lcms2 2.16
lensfun v0.3.95-1875-g8ce04d3f
libcdio-paranoia 10.2
libgme 0.6.4
libilbc v3.0.4-346-g6adb26d4a4
libjxl v0.11-snapshot-484-g8ce9537c
libopencore-amrnb 0.1.6
libopencore-amrwb 0.1.6
libplacebo v7.351.0-145-g1dcaea8
libsoxr 0.1.3
libssh 0.11.3
libtheora v1.2.0
libwebp v1.6.0-148-g4510224
openal-soft latest
openapv v0.2.1.0
openmpt libopenmpt-0.6.26-13-gd6ed539ce
opus v1.6.1-6-ga3f0ec02
qrencode 4.1.1
quirc 1.2
rav1e p20250624-3-g564ae3b
rist 0.2.12
rubberband v1.8.1
SDL release-2.32.0-157-g99eca2ca0
shaderc v2026.1-2-g1d234d3
shine 3.1.1
snappy 1.2.2
speex Speex-1.2.1-51-g0589522
srt v1.5.5-rc.0a-11-g2220de50
SVT-AV1 v4.0.1
SVT-JPEG-XS v0.9.0-40-gb1b2278
twolame 0.4.0
uavs3d v1.1-50-g0e20d2c
VAAPI 2.24.0.
vidstab v1.1.1-24-g92bc0b0
vmaf v3.0.0-124-g332dde62
vo-amrwbenc 0.1.3
vorbis v1.3.7-22-g2d79800b
VPL 2.16
vpx v1.16.0-30-g53b5de7d7
vulkan-loader v1.4.342
vvenc v1.14.0-7-g169a86c
whisper.cpp 1.8.3
x264 v0.165.3223
x265 4.1-223-gafa0028dd
xavs2 1.4
xevd 0.5.0
xeve 0.5.1
xvid v1.3.7
zeromq 4.3.5
zimg release-3.0.6-211-gdf9c147
zvbi v0.2.44-4-g41477c9

