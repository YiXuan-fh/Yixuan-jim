package org.jim.core.packets;

/**
 * <pre>
 *设备类型
 * </pre>
 * enum {@code DeviceType}
 */
public enum DeviceType{
  /**
   * <code>DEVICE_TYPE_UNKNOW = 0;</code>
   */
  DEVICE_TYPE_UNKNOW(0),
  /**
   * <pre>
   *PC
   * </pre>
   *
   * <code>DEVICE_TYPE_PC = 1;</code>
   */
  DEVICE_TYPE_PC(1),
  /**
   * <pre>
   *安卓
   * </pre>
   *
   * <code>DEVICE_TYPE_ANDROID = 2;</code>
   */
  DEVICE_TYPE_ANDROID(2),
  /**
   * <pre>
   *IOS
   * </pre>
   *
   * <code>DEVICE_TYPE_IOS = 3;</code>
   */
  DEVICE_TYPE_IOS(3),
  ;

  public final int getNumber() {
    return value;
  }

  public static DeviceType valueOf(int value) {
    return forNumber(value);
  }

  public static DeviceType forNumber(int value) {
    switch (value) {
      case 0: return DEVICE_TYPE_UNKNOW;
      case 1: return DEVICE_TYPE_PC;
      case 2: return DEVICE_TYPE_ANDROID;
      case 3: return DEVICE_TYPE_IOS;
      default: return null;
    }
  }

  private final int value;

  private DeviceType(int value) {
    this.value = value;
  }
}

