package PojoClass;

public class _02_GoRestCreatingPost {

    // to be able to create a post we need 4 entries
    /*
        0- will be provided by GoRest id for the post
        1- user_id that will be given when user is going to be created
        2- title
        3-body
     */

   private String title;
   private String body;

   private String postID;

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setTitle(String title) {
       this.title=title;
   }

   public String getTitle() {
       return title;
   }

   public void setBody(String body) {
       this.body=body;
   }

   public String getBody() {
       return body;
   }


}
