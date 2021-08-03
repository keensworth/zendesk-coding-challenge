package zendesk.api;

/**
 * Represents all of the relevant ticket data returned
 * when requesting a ticket from the Zendesk API
 */
public class Ticket {

    public Ticket(){};
    // Zendesk attributes
    public boolean allow_attachments;
    public boolean allow_channelback;
    public boolean has_incidents;
    public boolean is_public;
    public long assignee_id;
    public long brand_id;
    public long forum_topic_id;
    public long group_id;
    public long id;
    public long organization_id;
    public long problem_id;
    public long requester_id;
    public long submitter_id;
    public long ticketForm_id;
    public long viaFollowupSource_id;
    public long[] collaborator_ids;
    public long[] email_cc_ids;
    public long[] follower_ids;
    public long[] followup_ids;
    public String created_at;
    public String description;
    public String due_at;
    public String external_id;
    public String priority;
    public String raw_subject;
    public String recipient;
    public String status;
    public String subject;
    public String type;
    public String updated_at;
    public String url;
    public String[] collaborators;
    public String[] custom_fields;
    public String[] macro_ids;
    public String[] sharing_agreement_ids;
    public String[] tags;

    // Custom attributes
    public String assigneeName;
    public String requesterName;

}
