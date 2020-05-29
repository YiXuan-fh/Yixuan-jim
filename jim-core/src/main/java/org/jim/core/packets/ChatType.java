package org.jim.core.packets;

/**
 * <pre>
 **
 * 聊天类型
 * </pre>
 */
public enum ChatType{
  /**
   * <pre>
   *未知
   * </pre>
   *
   * <code>CHAT_TYPE_UNKNOWN = 0;</code>
   */
  CHAT_TYPE_UNKNOWN(0),
  /**
   * <pre>
   *公聊
   * </pre>
   *
   * <code>CHAT_TYPE_PUBLIC = 1;</code>
   */
  CHAT_TYPE_PUBLIC(1),
  /**
   * <pre>
   *私聊
   * </pre>
   *
   * <code>CHAT_TYPE_PRIVATE = 2;</code>
   */
  CHAT_TYPE_PRIVATE(2),
  ;

  public final int getNumber() {
    return value;
  }

  public static ChatType valueOf(int value) {
    return forNumber(value);
  }

  public static ChatType forNumber(int value) {
    switch (value) {
      case 0: return CHAT_TYPE_UNKNOWN;
      case 1: return CHAT_TYPE_PUBLIC;
      case 2: return CHAT_TYPE_PRIVATE;
      default: return null;
    }
  }

  private final int value;

  ChatType(int value) {
    this.value = value;
  }
}

