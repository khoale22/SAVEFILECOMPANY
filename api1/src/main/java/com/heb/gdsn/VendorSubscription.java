package com.heb.gdsn;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a message to send to 1WorldSync with the subscription status of a particular vendor. Forms the basis
 * of a CIS GDSN message.
 *
 * @author d116773
 * @since 2.3.0
 */
@Entity
@Table(name="vend_subscription")
public class VendorSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="sequence_number")
    private int sequenceNumber;

    @Column(name="provider_gln")
    private String vendorGln;

    @Formula("TRIM(LEADING '0' FROM provider_gln)")
    private String trimmedVendorGln;

    @Column(name="provider_name")
    private String vendorName;

    @Column(name="subscription_status")
    private String subscriptionStatus;

    @Column(name="status")
    private String messageStatus;

    @Column(name="create_time")
    private LocalDateTime createTime;

    @Column(name="process_time")
    private LocalDateTime processTime;

    @Column(name="create_user")
    private String createUserId;

    @Column(name="message_id")
    private String messageId;

    @Column(name="document_id")
    private String documentId;

    @Column(name="cic_received_sw")
    private boolean sendReceivedCic;

    @Column(name="cic_review")
    private boolean sendReviewCic;

    @Column(name="cic_syncronized")
    private boolean sendSynchronizedCic;

    // Don't know why, but the order of these is significant and it doesn't work when message_id is first.
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "document_id", insertable = false, updatable = false),
            @JoinColumn(name = "message_id", insertable = false, updatable = false)
    })
    private Message message;

    /**
     * Compares this object with another for equality.
     *
     * @param o The object to compare to.
     * @return True if the objects are equal and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VendorSubscription)) return false;

        VendorSubscription that = (VendorSubscription) o;

        return sequenceNumber == that.sequenceNumber;

    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return sequenceNumber;
    }

    /**
     * Returns the VendorGln has been removed leading zero.
     * @return The VendorGln has been removed leading zero for this object.
     */
    public String getTrimmedVendorGln() {
        return trimmedVendorGln;
    }

    /**
     * Sets the VendorGln with removed leading zero for this object.
     *
     * @param trimmedVendorGln The VendorGln removed leading zero for this object.
     */
    public void setTrimmedVendorGln(String trimmedVendorGln) {
        this.trimmedVendorGln = trimmedVendorGln;
    }

    /**
     * Returns the unique ID for this object.
     *
     * @return The unique ID for this object.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Sets the unique ID for this object.
     *
     * @param sequenceNumber The unique ID for this object.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Returns the GLN number for the vendor this message is for.
     *
     * @return The GLN number for the vendor this message is for.
     */
    public String getVendorGln() {
        return vendorGln;
    }

    /**
     * Sets the GLN number for the vendor this message is for.
     *
     * @return The GLN number for the vendor this message is for.
     */
    public void setVendorGln(String vendorGln) {
        this.vendorGln = vendorGln;
    }

    /**
     * Returns the name of the vendor this message is for.
     *
     * @return The name of the vendor this message is for.
     */
    public String getVendorName() {
        return vendorName;
    }

    /**
     * Sets the name of the vendor this message is for.
     *
     * @return The name of the vendor this message is for.
     */
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    /**
     * Returns the status of the subscription this message is for.
     *
     * @return The status of the subscription this message is for.
     */
    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    /**
     * Sets the status of the subscription this message is for.
     *
     * @return The status of the subscription this message is for.
     */
    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    /**
     * Returns the status of this message. This if from the perspective of the HEB application, and not the GDSN status
     * of the subscription.
     *
     * @return The status of this message.
     */
    public String getMessageStatus() {
        return messageStatus;
    }

    /**
     * Sets the status of this message.
     *
     * @return The status of this message.
     */
    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    /**
     * Returns the time this record was created.
     *
     * @return The time this record was created.
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * Sets the time this record was created.
     *
     * @param createTime The time this record was created.
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * Returns the time this record was processed.
     *
     * @return The time this record was processed.
     */
    public LocalDateTime getProcessTime() {
        return processTime;
    }

    /**
     * Sets the time this record was processed.
     *
     * @return The time this record was processed.
     */
    public void setProcessTime(LocalDateTime processTime) {
        this.processTime = processTime;
    }

    /**
     * Returns the one-pass ID of the user who created this record.
     *
     * @return The one-pass ID of the user who created this record.
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * Sets the one-pass ID of the user who created this record.
     *
     * @return The one-pass ID of the user who created this record.
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * Returns the document ID of the message that sent this record to 1WorldSync.
     *
     * @return The document ID of the message that sent this record to 1WorldSync.
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document ID of the message that sent this record to 1WorldSync.
     *
     * @return The document ID of the message that sent this record to 1WorldSync.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * Returns the message ID of the message that sent this record to 1WorldSync.
     *
     * @return The message ID of the message that sent this record to 1WorldSync.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the message ID of the message that sent this record to 1WorldSync.
     *
     * @return The message ID of the message that sent this record to 1WorldSync.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Returns the object that tracks the GDSN message this record is tied to.
     *
     * @return The object that tracks the GDSN message this record is tied to.
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Sets the object that tracks the GDSN message this record is tied to.
     *
     * @param message The object that tracks the GDSN message this record is tied to.
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * Returns whether or not to send RECEIVED CICs for this vendor.
     *
     * @return True if you should send RECEIVED CICs and false otherwise.
     */
    public boolean isSendReceivedCic() {
        return sendReceivedCic;
    }

    /**
     * Sets whether or not to send RECEIVED CICs for this vendor.
     *
     * @param sendReceivedCic True if you should send RECEIVED CICs and false otherwise.
     */
    public void setSendReceivedCic(boolean sendReceivedCic) {
        this.sendReceivedCic = sendReceivedCic;
    }

    /**
     * Returns whether or not to send REVIEW CICs for this vendor.
     *
     * @return True if you should send REVIEW CICs and false otherwise.
     */
    public boolean isSendReviewCic() {
        return sendReviewCic;
    }

    /**
     * Sets whether or not to send REVIEW CICs for this vendor.
     *
     * @param sendReviewCic True if you should send REVIEW CICs and false otherwise.
     */
    public void setSendReviewCic(boolean sendReviewCic) {
        this.sendReviewCic = sendReviewCic;
    }

    /**
     * Returns whether or not to send SYNCHRONIZED CICs for this vendor.
     *
     * @return True if you should send SYNCHRONIZED CICs and false otherwise.
     */
    public boolean isSendSynchronizedCic() {
        return sendSynchronizedCic;
    }

    /**
     * Sets whether or not to send SYNCHRONIZED CICs for this vendor.
     *
     * @param sendSynchronizedCic True if you should send SYNCHRONIZED CICs and false otherwise.
     */
    public void setSendSynchronizedCic(boolean sendSynchronizedCic) {
        this.sendSynchronizedCic = sendSynchronizedCic;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    @Override
    public String toString() {
        return "VendorSubscription{" +
                "sequenceNumber=" + sequenceNumber +
                ", vendorGln='" + vendorGln + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", subscriptionStatus='" + subscriptionStatus + '\'' +
                ", messageStatus='" + messageStatus + '\'' +
                ", createTime=" + createTime +
                ", processTime=" + processTime +
                ", createUserId='" + createUserId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", documentId='" + documentId + '\'' +
                ", sendReceivedCic=" + sendReceivedCic +
                ", sendReviewCic=" + sendReviewCic +
                ", sendSynchronizedCic=" + sendSynchronizedCic +
                '}';
    }

    /**
     * Returns the default sort for this class (provider GLN ascending).
     *
     * @return The default sort for this class.
     */
    public static Sort getDefaultSort() {
        return new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
    }
}
