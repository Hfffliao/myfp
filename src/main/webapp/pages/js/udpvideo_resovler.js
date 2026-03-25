let rawbuffer = new Uint8Array(0);
let hashmap = new Map();
let completePicIndexs = new Map();//记录每帧图片的起始标志在hashmap中位置
let finalCompletePicIndexs = new Map();//记录每帧图片的起始标志在hashmap中位置
let finalIndex = 0;//记录hashmap中最后一个包的序号
let startIndex = 0;//记录哪一个hashmap正在被读到dataFromHashmap里
let insertHashCount = 0;//记录udp包被插入到hashmap中的次数


//用这个结构体来记录完整的图片在哪几个udp包
class IntPair {
    constructor(int1, int2) {//int1是包图片头的序号，int2是图片包含包的个数
        this.int1 = int1; // 第一个整数
        this.int2 = int2; // 第二个整数
    }
}
//2025.11.1
const workerCode = `
// 视频处理Worker
let lastFrameTime = 0;
const frameInterval = 0;
let frameBuffer = [];
let combined = new Uint8Array(0);
let totalLength = 0;
const MAX_FRAME_SIZE = 20000;
let board = 0;
//MAX_FRAME_SIZE * 2;
const LOG_ENABLED = false; // 设为false即可关闭所有日志

    // 替换所有console.log为条件输出
    function log(...args) {
        if (LOG_ENABLED) {
            console.log(...args);
        }
    }  


    self.onmessage = function(e) {
        //console.error("线程2接到数据:"+Date.now()); 
        combined = e.data;  
        // 输出：1725524400000（示例值）
        // frameBuffer.push(combined);
        // frameBuffer.push(new Uint8Array(data));
        // let offset = 0;
        // 1. 将新接收的数据添加到缓冲区
        //totalLength = frameBuffer.reduce((sum, arr) => sum + arr.length, 0);
        //combined=new Uint8Array(totalLength);
        // for (const arr of frameBuffer) {
        //     combined.set(arr, offset);
        //     offset += arr.length;
        // }
        //frameBuffer = [];
        let booleanMain=true;//用于判断是否要退出while循环
        while(true){
            if (isFrameComplete()) {
                // 处理完整帧数据
                booleanMain=processFrame();
                if(!booleanMain){
                    booleanMain=!booleanMain;
                    break;
                }
            }else{                      
                log("且是combined.length:"+combined.length);
                break;
            }
        }
        //console.log("线程2结束:"+Date.now()); // 输出：1725524400000（示例值）
        //判断缓冲区是否要处理
        function isFrameComplete(){
            if(combined.length < board){
                //console.log("conbined小于一帧:"+combined.length+"board:"+board);
                return false;
            }else if(combined.length > 1024*1024){//conbined太大了，处理不过来的
               // console.log("combined大于20kb:"+combined.length);
                combined=new Uint8Array(0);
                return false;
            }else{
                return true;
            }
        }
        //处理并渲染图像
        function processFrame(){
            const now = Date.now();
            if (now - lastFrameTime < frameInterval) return;
            lastFrameTime = now;
            let startIndex=0;
            let lengthStartBytes=0;
            let imagelength=0;
            let bytes =new Uint8Array(4);
            //找到picStartindex；
            let j=0;
            for(let i=0;i<combined.length;i++){
                j=0;
                while(combined[i+j] == 119+j){
                    j++;
                }
                if(j==8){
                    log("找到图像第一个标志位");
                   // console.log("combined:"+combined.length);
                   // console.log("i:"+i);
                    startIndex=12;
                    lengthStartBytes=8;
                    combined=combined.subarray(i);
                    //log("j:"+j)
                    break;
                }
            }  
            if(j!=8){//表示一轮下来没有找到图像第一个标志位
                console.error("i!=8，一轮下来没有找到图像第一个标志位");
                combined=new Uint8Array(0);//既然这些数据都没有起始位，那就不可能被显示
                return false;
            }
            //获取表示图像长度的四个字节
            for(let i=0;i<4;i++){
                bytes[i]=combined[lengthStartBytes+i];
            }
            imagelength=bytesToUInt32BE(bytes);
            if(imagelength>MAX_FRAME_SIZE||imagelength<0){
                console.error("imagelength解析错误:"+imagelength);
                for(let i=0;i<13;i++){
                    console.log(i+":"+combined[i]);
                }
                return false;
            }
            if(imagelength+12>combined.length){//图像比实际的小300字节，我也放他过去了
                console.error("imagelength:"+imagelength+"conbined长度短于image长度："+combined.length);
                //console.error();
                //console.error("startIndex:"+startIndex);
                //board=imagelength+12;
                return false;
            }
            bytes =new Uint8Array(4);
            log("imagelength:"+imagelength);
            log("startIndex:"+startIndex);
            log("lengthStartBytes:"+lengthStartBytes);
           
            //console.log("combined.length:"+combined.length);
            self.postMessage(combined.subarray(startIndex,startIndex+imagelength));  // 发送 Uint8Array 到主线程
            //console.log("线程2处理好照片交给主线程显示");
            // 6. 移除已处理的帧数据，保留剩余缓冲区内容
            //获取下一帧的长度，开始位置
        }
        function bytesToUInt32BE(bytes) {
        return (bytes[0] << 24) | 
                (bytes[1] << 16) | 
                (bytes[2] << 8) | 
                (bytes[3]) >>> 0;
        }
        
    };
  
`;
// 图像处理函数
function displayImage(uint8Array, mimeType = 'image/jpeg') {
    const beginTime = Date.now();
    try {
        if (uint8Array.length == 0) {
            throw new Error("图像uint8Array.length == 0"); // 触发catch块
        }
        ////检查 jpeg图像尾标志位
        if (uint8Array[uint8Array.length - 2] != 255 || uint8Array[uint8Array.length - 1] != 217) {
            // return;
            console.error("图像uint8Array[uint8Array.length-2]!=255||uint8Array[uint8Array.length-1]!=217");
            return;//下面是向前找图像尾标志位
            // //     log("imagelength:"+imagelength);
            // //    log("startIndex:"+startIndex);
            // //     log("lengthStartBytes:"+lengthStartBytes);
            // //     log("uint8Array.length:"+uint8Array.length);
            // //     log("combined.length:"+combined.length);
            // let index = uint8Array.length - 1;
            // //查找jpeg图像尾标志位
            // while (index > uint8Array.length - 300) {//图像小于长度的一半就不查找了
            //     if (uint8Array[index - 1] != 255 || uint8Array[index] != 217) {
            //         index--;
            //     } else {
            //         break;
            //     }
            // }
            // if (index == uint8Array.length - 300) {
            //     //console.log("uint8Array.length:"+uint8Array.length);
            //     //                           // 调试 将循环日志放入可折叠组
            //     // console.groupCollapsed(`字节数组 (长度: ${uint8Array.length})`);
            //     // for(let i=0; i<uint8Array.length; i++){
            //     //     console.log(`[${i}]: ${uint8Array[i]}`);
            //     // }
            //     // console.groupEnd();
            //     throw new Error("找不到jpeg图像尾标志位"); // 触发catch块
            // } else {
            //     log("找到jpeg图像尾标志位index:" + index);
            // }
            // // 移除无效数据
            // uint8Array = uint8Array.subarray(0, index + 1);
            // //   // 调试 将循环日志放入可折叠组
            // //     console.groupCollapsed(`字节数组 (长度: ${uint8Array.length})`);
            // //     for(let i=0; i<uint8Array.length; i++){
            // //         console.log(`[${i}]: ${uint8Array[i]}`);
            // //     }
            // //     console.groupEnd();

        }
        ////检查 jpeg图像头标志位
        if (uint8Array[0] != 255 || uint8Array[1] != 216) {
            throw new Error("图像uint8Array[0]!=255||uint8Array[1]!=216"); // 触发catch块
        }

        // 创建 Blob 对象
        const blob = new Blob([uint8Array], { type: mimeType });
        //用blob创建图像
        // // 使用createImageBitmap提高解码性能
        // createImageBitmap(blob).then(imageBitmap => {
        //     const canvas = document.createElement('canvas');
        //     const ctx = canvas.getContext('2d');

        //     // 设置旋转后的尺寸
        //     canvas.width = 320;
        //     canvas.height = 240;

        //     // 执行旋转
        //     ctx.save();
        //     ctx.translate(canvas.width / 2, canvas.height / 2);
        //     ctx.rotate(-90 * Math.PI / 180);
        //     ctx.drawImage(imageBitmap, -imageBitmap.width / 2, -imageBitmap.height / 2);
        //     ctx.restore();

        //     // 直接设置src，避免额外URL创建
        //     imgElement.src = canvas.toDataURL();
        // })
        //  生成临时 URL
        const url = URL.createObjectURL(blob);

        window.imgelement.src = url;

        const haoshi = Date.now() - beginTime;
        if (haoshi > 2) {
            console.log("displayimage:" + "耗时:" + haoshi);
        }

    } catch (error) {
        console.error("显示图像错误:" + error);
    }
}

function addmessage(message) {
    //onmessageCount1++; //记录onmessage次数
    //console.log("开始给rawbuffer加数据:"+Date.now()); // 输出：1725524400000（示例值）

    //把来的数据加到缓冲里
    const newData = new Uint8Array(message);
    //console.log("newData.length:"+newData.length);

    // console.groupCollapsed(`字节数组 (长度: ${newData.length})`);
    // for(let i=0; i<newData.length; i++){
    //     console.log(`[${i}]: ${newData[i]}`);
    // }
    // console.groupEnd();

    const newBuffer = new Uint8Array(rawbuffer.length + newData.length);

    newBuffer.set(rawbuffer, 0);            // 复制旧数据
    newBuffer.set(newData, rawbuffer.length); // 追加新数据

    rawbuffer = newBuffer;
    log("rawbuffer.length:" + rawbuffer.length);

    // 更新rawbuffer
    // if(rawbuffer.length<1436*2){
    //     log("rawbuffer.length<1436*2，小于两个udp包，就不处理");
    //     return;
    // }
    //console.log("开始给hashmap加数据:"+Date.now()); // 输出：1725524400000（示例值）

    let index = 0;
    //console.log("rawbuffer.length:"+rawbuffer.length);

    //查找rawbuffer中的包，把包加到hashmap里
    while (index < rawbuffer.length) {
        // console.groupCollapsed(`字节数组 (长度: ${rawbuffer.length})`);
        // for(let i=0;i<rawbuffer.length;i++){
        //     console.log("rawbuffer["+i+"]:"+rawbuffer[i+index]);
        // }
        // console.groupEnd();
        //console.log("rawbuffer.length:"+rawbuffer.length);
        //console.log("index:"+index);
        if (rawbuffer[index] == 243 && rawbuffer[index + 1] == 126 && rawbuffer[index + 2] == 98 && rawbuffer[index + 3] == 211) {
            // for(let i=0;i<10;i++){
            //     console.log("rawbuffer["+i+"]:"+rawbuffer[i+index]);
            // }
            log("rawbuffer[index]:" + rawbuffer[index]);

            //找到了一个包的起始标志
            rawbuffer = rawbuffer.subarray(index);
            //key是包的序号
            let key = rawbuffer[4] * 256 + rawbuffer[5];
            //length是包的长度

            let length = rawbuffer[8] * 256 + rawbuffer[9];
            //剩余数据长度小于包的长度，就不处理，rawbuffer继续累计数据去吧
            if (length > rawbuffer.length - 10) {
                console.log("length>rawbuffer.length-10，包长度大于剩余数据长度");

                break;
            }
            //value是包的内容，
            log("rawbuffer.length:" + rawbuffer.length);
            log("length:" + length);
            log("rawbuffer.length:" + rawbuffer.length);
            //找到picStartindex；
            let j = 0;
            for (let i = 10; i < 18; i++) {//从第10个字节开始查找(也就是跳过udp包的起始标志)，查找图像第一个标志位
                j = 0;
                while (rawbuffer[i + j] == 119 + j) {
                    j++;
                }
                if (j == 8) {
                    //console.log("找到图像第一个标志位");
                    // console.log("combined:"+combined.length);
                    // console.log("i:"+i);
                    //log("j:"+j)
                    const picLength = rawbuffer[20] * 256 + rawbuffer[21];//获取图片长度(单位是字节且是大端序且是uint16_t类型)
                    const picPackageCount = Math.ceil(picLength / 1334);
                    //console.log("push"+key);
                    const picheader = key;

                    const structData = new IntPair(picheader, picPackageCount);
                    completePicIndexs.set(picheader, structData);
                    //console.log("completePicIndexs:"+key);
                    break;
                }
            }
            //completePicIndexs.push(key);
            // console.log("completePicIndexs:"+key+"时间："+Date.now());
            insertHashCount++; //记录insertHashCount次数
            let value = rawbuffer.subarray(10, 10 + length);
            log("value.length:" + value.length);
            hashmap.set(key, value);
            //console.log("hashmap:" + key);

            log("value.length:" + value.length);
            //更新finalIndex
            if (key > finalIndex) {
                finalIndex = key;
            }
            //删除rawbuffer中已经处理过的部分
            rawbuffer = rawbuffer.subarray(10 + length);
            //重置index
            index = 0;
            //如果是第一次onmessage，告诉程序从第一次到的包序号开始处理
            if (insertHashCount == 1) {
                startIndex = key;
            }

        } else {
            index++;
        }
    }
    //检查hashmap中是否有缺失的包
    //从startIndex开始检查hashmap中的数据，直到hashmap中没有数据为止
    //把接收到的完整的图片是哪个udp包到哪个udp包标出来
    markCompletedPic(hashmap, completePicIndexs, finalCompletePicIndexs, startIndex, finalIndex);//执行了之后finalCompletePicIndexs
    // 里面就有完整的图片的起始标志和包的个数

    let dataFromHashmap = new Uint8Array(0);
    const lastStartIndex = startIndex;
    for (let i = startIndex; i <= finalIndex; i++) {

        if (finalCompletePicIndexs.has(i)) {
            const structData = finalCompletePicIndexs.get(i);
            const picheader = structData.int1;
            const picPackageCount = structData.int2;
            for (let j = 0; j < picPackageCount; j++) {
                if (!hashmap.has(i + j)) {
                    console.error("finalConletePicIndex有缺失的包");
                    break;
                }
                //把hashmap中的数据加到dataFromHashmap中
                const newDatax = hashmap.get(i + j);
                log("i:" + i);
                //log("finalIndex:"+fangrudata);
                log("newDatax:" + newDatax.length)
                const newLength = dataFromHashmap.length + newDatax.length;

                // 创建新数组
                const newEventData = new Uint8Array(newLength);

                // 复制旧数据到新数组开头
                newEventData.set(dataFromHashmap, 0);

                // 添加新数据到旧数据末尾
                newEventData.set(newDatax, dataFromHashmap.length);

                // 更新引用
                dataFromHashmap = newEventData;
                //console.log("dataFromHashmap.length:"+dataFromHashmap.length);

            }
            startIndex = i + picPackageCount - 1;
            finalCompletePicIndexs.delete(i);
        }
    }
    for (let i = lastStartIndex; i < startIndex; i++) {
        completePicIndexs.delete(i);
        hashmap.delete(i);
    }

    if (dataFromHashmap.length == 0) {

        return;
    }
    //console.log("主线程结束:" + dataFromHashmap.length);
    window.videoWork.postMessage(dataFromHashmap, [dataFromHashmap.buffer]);

    dataFromHashmap = new Uint8Array(0);

}

function markCompletedPic(hashmap, completePicIndexs, finalCompletePicIndexs, startIndex, finalIndex) {
    const INstartIndex = startIndex;
    const INfinalIndex = finalIndex;
    for (let i = startIndex; i <= finalIndex; i++) {
        if (completePicIndexs.has(i)) {
            const structData = completePicIndexs.get(i);
            const picheader = structData.int1;
            const picPackageCount = structData.int2;
            //检查hashmap中是否有缺失的包
            for (let j = 0; j < picPackageCount; j++) {

                if (!hashmap.has(i + j)) {
                    break
                }
                if (j == picPackageCount - 1) {
                    finalCompletePicIndexs.set(picheader, new IntPair(picheader, picPackageCount));
                }
            }
        }
    }
}

//completePicIndexs标记了一个个图片在hashmap里面的位置
//hashmap里面是一个个udp包
//这个函数负责根据completePicIndexs里面的信息，把hashmap里面完整的图片的udp包找出来
//条件
// 确保有至少有两个数，且递增，且从零索引开始，且连续
//返回-1表示数在整个数组的左边
//返回-2表示数在整个数组的右边
//(返回正数表示在这个下标的右边，下一个下边的左边)或者在这个下标