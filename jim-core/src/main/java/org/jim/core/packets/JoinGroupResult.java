package org.jim.core.packets;

/**
 * <pre>
 **
 * 加入群组申请的结果
 * </pre>
 * enum {@code JoinGroupResult}
 */
public enum JoinGroupResult{
  /**
   * <pre>
   *不允许进入，原因为其它
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_UNKNOWN = 0;</code>
   */
  JOIN_GROUP_RESULT_UNKNOWN(0),
  /**
   * <pre>
   *允许进入
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_OK = 1;</code>
   */
  JOIN_GROUP_RESULT_OK(1),
  /**
   * <pre>
   *组不存在
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_NOT_EXIST = 2;</code>
   */
  JOIN_GROUP_RESULT_NOT_EXIST(2),
  /**
   * <pre>
   *组满
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_GROUP_FULL = 3;</code>
   */
  JOIN_GROUP_RESULT_GROUP_FULL(3),
  /**
   * <pre>
   *在黑名单中
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_IN_BACKLIST = 4;</code>
   */
  JOIN_GROUP_RESULT_IN_BACKLIST(4),
  /**
   * <pre>
   *被踢
   * </pre>
   *
   * <code>JOIN_GROUP_RESULT_KICKED = 5;</code>
   */
  JOIN_GROUP_RESULT_KICKED(5),
  ;

  public final int getNumber() {
    return value;
  }

  public static JoinGroupResult valueOf(int value) {
    return forNumber(value);
  }

  public static JoinGroupResult forNumber(int value) {
    switch (value) {
      case 0: return JOIN_GROUP_RESULT_UNKNOWN;
      case 1: return JOIN_GROUP_RESULT_OK;
      case 2: return JOIN_GROUP_RESULT_NOT_EXIST;
      case 3: return JOIN_GROUP_RESULT_GROUP_FULL;
      case 4: return JOIN_GROUP_RESULT_IN_BACKLIST;
      case 5: return JOIN_GROUP_RESULT_KICKED;
      default: return null;
    }
  }

  private final int value;

  private JoinGroupResult(int value) {
    this.value = value;
  }
}

