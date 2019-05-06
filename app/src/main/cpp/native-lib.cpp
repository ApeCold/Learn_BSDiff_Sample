#include <jni.h>
#include <string>


extern "C" {
    extern int bs_main(int argc,char * argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_cn_bsd_learn_bsdiff_sample_MainActivity_bsPatch(JNIEnv *env, jobject instance, jstring oldApk_,
                                                     jstring patch_, jstring output_) {
    //把java字符串转为c/c++字符串
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    //合成？？？
    //int a[] = {1,2,3,5}
    char * argv[] = {"", const_cast<char *>(oldApk), const_cast<char *>(output),
                     const_cast<char *>(patch)};
    bs_main(4,argv);


    //释放指针（字符串）
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}