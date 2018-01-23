项目视频介绍 
===========

|#|标题|视频地址|
|---|----|-----|
|1|01-Banner广告轮播图的介绍|http://www.iqiyi.com/w_19ruukqegl.html|
|2|02-指示器的实现并导入项目到GitHub|http://www.iqiyi.com/w_19ruulmt0t.html|
|3|03-ViewPager无线轮播实现原理和代码编写|http://www.iqiyi.com/w_19ruv7ykv1.html|
|4|04-Banner封装成Android Libray库|http://www.iqiyi.com/w_19ruvwqut9.html|
|5|05-Banner生成依赖Android库|http://www.iqiyi.com/w_19ruvoczxx.html|
|6|06-Android轮播图错误代码修复|http://www.iqiyi.com/w_19rus14sdt.html|
|7|07-Banner无限轮播优化|http://www.iqiyi.com/w_19rwf8xeo1.html#vfrm=16-1-1-1|





有道云笔记
http://note.youdao.com/noteshare?id=c71dbc8cb3d5afec1439d2c81cd21f45

第一步 先加入这个库的地址
========================
allprojects {
		repositories {
			
			maven { url 'https://www.jitpack.io' }
		}
	}


第二步  引用Android无限轮播图依赖库
=================================
dependencies {
	        compile 'com.github.githubwwj:BannerLearn:0.2'
}

