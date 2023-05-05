#include <jni.h>

extern "C" {
    JNIEXPORT jstring JNICALL
    Java_com_dzulfaqar_quranku_core_utils_Constant_passphrase(
            JNIEnv *env, jobject instance
    ) {
        return env->NewStringUTF("qOj1X5e#XXRLeVmd@Q7CqA%HVDTLLCGBPfjkTqvU*QBMIXZgl8!75l0QXs4w8Y@ZCg&aIuJEaqV@Ik#VTwWfOuf5Xgc6RWz8lKO");
    }
}
