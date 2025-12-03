//const endpointPath = "http://linyi.love";
// const endpointPath = "http://localhost:25571";
//const endpointPath = "http://localhost:8080";
const endpointPath = "https://linyi.love:8443";
//const Coderoot='C:/uploads';
//const Coderoot='C:/uploads';

if (document.readyState === 'interactive' || document.readyState === 'complete') {
    // DOM 已经加载完成，直接执行初始化逻辑

    initPage();
} else {
    // 监听 DOMContentLoaded 事件
    document.addEventListener('DOMContentLoaded', initPage);
}
// 添加汉堡菜单切换功能
function toggleMenu() {
    const navLinks = document.getElementById('navLinks');
    navLinks.style.display = navLinks.style.display === 'flex' ? 'none' : 'flex';
}

// 点击菜单外区域关闭菜单（可选）
document.addEventListener('click', (e) => {
    if (!e.target.closest('.nav-container')) {
        document.getElementById('navLinks').style.display = 'none';
    }
});

async function copyToClipboard() {
    // 获取要复制的文本内容
    const text = document.getElementById('copyContent').innerText;

    try {
        // 写入剪贴板
        await navigator.clipboard.writeText(text);
        alert('复制成功: ' + text);
    } catch (err) {
        // 错误处理
        console.error('复制失败:', err);
        alert('请手动复制内容');
    }
}

async function initPage() {//初始化导航栏和页脚
  //  console.log('initPage：页面加载开始');
    await loadHeaderFooter();
    //console.log('initPage：页面加载完成');
    getUserInfo();
    initnavbar();
    let lastScrollTop = 0;
    
    const navbar = document.querySelector('.navbar');//
    const initialNavbarTop = navbar.offsetTop; // 记录导航栏初始的 top 位置
    //添加上下滑动导航栏对应的行为
    window.addEventListener('scroll', function () {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

        if (scrollTop > lastScrollTop) {
            // 向下滚动，移除固定类，让导航栏跟随页面滚动
            navbar.classList.remove('fixed');
            // 确保导航栏在初始位置之下时，恢复绝对定位
            if (scrollTop < initialNavbarTop) {
                navbar.style.top = initialNavbarTop + 'px';
            }
        } else {
            // 向上滚动，添加固定类，将导航栏固定在视口顶部
            if (scrollTop >= initialNavbarTop) {
                navbar.classList.add('fixed');
            } else {
                navbar.classList.remove('fixed');
                navbar.style.top = initialNavbarTop + 'px';
            }
        }

        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop; // 确保滚动值不为负数
    });
}

function loadHeaderFooter() {
    const headFetch = fetch(contextPath+'/pages/css/head.html')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            // 将响应内容转换为文本
            return response.text();
        })
        .then(html => {
            // 将加载的 HTML 内容插入到指定容器中
            document.getElementById('header').innerHTML = html;
        })
        .catch(error => {
            console.error('加载头部文件时出错:', error);
            throw error;
        });
    const footFetch = fetch(contextPath+'/pages/css/foot.html')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            // 将响应内容转换为文本
            return response.text();
        })
        .then(html => {
            // 将加载的 HTML 内容插入到指定容器中
            document.getElementById('footer').innerHTML = html;
        })
        .catch(error => {
            console.error('加载底部文件时出错:', error);
            throw error;
        });
    return Promise.all([headFetch, footFetch]);
}
/**
 * 显示用户信息框，并处理退出登录逻辑
 * @param {Event} event - 触发该函数的事件对象
 */
function showUserInfo(event) {
    // 阻止事件的默认行为，避免链接跳转
    event.preventDefault();
    const userLink = document.getElementById('userLink');
    const userInfo = document.getElementById('userInfo');
    const rect = userLink.getBoundingClientRect();
    const logout=document.getElementById('logout');
    const usercontext=document.getElementById('usercontext');
    userInfo.style.display = 'flex';
    userInfo.style.top = rect.top + rect.height -2+ 'px';
    userInfo.style.left = rect.left + 'px';
    userInfo.style.width=rect.width*0.7+'px';
    logout.addEventListener('click',()=>{
        let url=`${endpointPath}/logout`
        fetch(url,{
            method:'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
        })
        .then(response=>{
            if(response.status!=200){
                throw Error(response.status);
            }else{
                return response.text();
            }

        })
        .then(text =>{
            if(text==='logout'){
                console.log(text);
                localStorage.removeItem('username');
                localStorage.removeItem('expirationDate');
                userInfo.style.display = 'none';
                getUserInfo();
            }else{
                throw new Error('200但是不是正确的返回值');
            }
            
        })
        .catch(error=>{
           // errorhandle(error);
            console.error('退出登录出错:', error);
        })
     
    })
}
// 从本地内存获取用户信息并且初始化用户信息表（点击用户名即可显示）
function getUserInfo() {
    const userInfo=document.getElementById('userInfo');
    const navLinks = document.getElementById('navLinks');
    const userLink = document.getElementById('userLink');
    
    const currentDate = new Date();
    const expirationDate = new Date(localStorage.getItem('expirationDate'));
    if(currentDate>expirationDate){
        localStorage.removeItem('username');
        localStorage.removeItem('expirationDate');
    }
    const username = localStorage.getItem('username');
    console.log('获取到的 username 类型:', typeof username);
    console.log('获取到的 username 值:', username);
    if (username !== null) {
        
        console.log(username);
        userLink.href = null;
        userLink.textContent = `welcome ${username}`;
        // 正确设置样式属性
        userLink.style.width = "50%";
        userLink.addEventListener('click', showUserInfo);
        userLink.addEventListener('mouseover',showUserInfo);
        //移出userLink和userInfo的时候把userInfo设为none
        navLinks.addEventListener('mouseout', (event) => {
            if (!userInfo.contains(event.relatedTarget) && !userLink.contains(event.relatedTarget)) {
                userInfo.style.display = 'none';
                console.log('鼠标同时移出两个元素');
                // 在这里添加你想要执行的逻辑
            }
        });
    } else {
        userLink.href = contextPath + "/pages/in.html";
        userLink.textContent = "登录";
        userLink.style.width = "12%";

        userLink.removeEventListener('click', showUserInfo);
        userLink.removeEventListener('mouseover',showUserInfo);
        navLinks.removeEventListener('mouseout', (event) => {
            if (!userInfo.contains(event.relatedTarget) && !userLink.contains(event.relatedTarget)) {
                userInfo.style.display = 'none';
                console.log('鼠标同时移出两个元素');
                // 在这里添加你想要执行的逻辑
            }
        });
        // alert("please login,请登录");
    }
}
function initnavbar() {
    // 初始化 logo 图片的 src 属性
    const logoImg = document.querySelector('.logo img');
    if (logoImg) {
        logoImg.src = `${contextPath}/pages/image/01.png`;
    }

    // 初始化各个导航链接的 href 属性
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        const linkText = link.textContent.trim();
        switch(linkText) {
            case '首页':
                link.href = `${contextPath}/pages/main.html`;
                break;
            case '服务':
                link.href = `${contextPath}/pages/fuwu.html`;
                break;
            case '关于':
                link.href = `${contextPath}/pages/man.html`;
                break;
            // 其他链接若后续有需求可继续添加 case
        }
    });
}
function errorhandle(error){
    let errorMessage = '文件下载出错，请稍后重试';
            // 尝试获取响应状态码
            if (error.response && error.response.status) {
                const statusCode = error.response.status;
                switch (statusCode) {
                    case 401:
                        errorMessage = '401：未授权，请先登录';
                        break;
                    case 403:
                        errorMessage = '403：禁止访问，您没有权限';
                        break;
                    case 404:
                        errorMessage = '404：文件未找到，请检查文件路径';
                        break;
                    case 500:
                        errorMessage = '500：服务器内部错误，请联系管理员';
                        break;
                    default:
                        errorMessage = `出错，状态码: ${statusCode}`;
                        break;
                }
            }
            // 显示错误信息弹窗
            alert(errorMessage);
}

