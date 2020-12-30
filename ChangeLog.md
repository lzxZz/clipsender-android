# 2020-12-30更新
1. 使用`SharedPreferences`替代`json`保存数据，避免`Files`下`Read`和`Write`操作需要`Android.O`以上sdk版本，兼容到低版本
2. 修复了在分享数据之后出现的短暂黑屏现象。
3. 修改最低适配安卓版本到4.4

