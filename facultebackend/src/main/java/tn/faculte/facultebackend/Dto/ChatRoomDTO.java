package tn.faculte.facultebackend.Dto;

public class ChatRoomDTO {
    private Long id;
    private Long senderId;
    private Long recipientId;

    public ChatRoomDTO() {
    }

    public ChatRoomDTO(Long id, Long senderId, Long recipientId) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
}
