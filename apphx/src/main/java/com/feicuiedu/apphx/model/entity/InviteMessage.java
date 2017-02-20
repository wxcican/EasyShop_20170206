package com.feicuiedu.apphx.model.entity;

/**
 * 好友邀请信息实体。此处为了简化，只考虑了好友邀请，没有考虑群组和聊天室邀请。
 * <p/>
 * 另外，环信发送好友邀请时，可以发送一条“理由”，这里为了简化，没有使用这一功能。
 * <p/>
 * <b>例如A加B为好友：A  --邀请--> B:</b>
 * <ol>
 * <li/>B收到邀请，B会存储一条邀请信息，此时为“未处理”({@link Status#RAW})状态；
 * <li/>B如果点击拒绝，消息会变成“已拒绝”({@link Status#REFUSED})状态；
 * <li/>B如果点击同意，消息会变成“已同意”({@link Status#ACCEPTED})状态；
 * <li/>B点击同意后，A会收到一条反馈消息，此时A存储的这条消息为“对方已接受”({@link Status#REMOTE_ACCEPTED})状态。
 * </ol>
 * <p/>
 * 详细的处理参见{@link com.feicuiedu.apphx.model.HxContactManager}.
 */
public class InviteMessage {

    private String fromHxId; // 发方环信id
    private String toHxId; // 收方环信id
    private Status status; // 当前消息状态

    @SuppressWarnings("unused")
    public InviteMessage() {
        // 无参构造方法是为了给Gson序列化和反序列化时使用。
    }

    public InviteMessage(String fromHxId, String toHxId, Status status) {
        this.fromHxId = fromHxId;
        this.toHxId = toHxId;
        this.status = status;
    }

    public String getFromHxId() {
        return fromHxId;
    }

    public String getToHxId() {
        return toHxId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        RAW, // 未处理
        ACCEPTED, // 已同意
        REFUSED, // 已拒绝
        REMOTE_ACCEPTED, // 对方已接受
    }
}
