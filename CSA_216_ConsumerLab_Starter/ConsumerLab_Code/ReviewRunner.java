class ReviewRunner {
  public static void main(String[] args) 
  {
    /* your code here, for example: */
    System.out.println(Review.sentimentVal("good"));
    System.out.println("Total Sentiment: " + Review.totalSentiment("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/SimpleReview.txt"));
    System.out.println("Star Rating: " + Review.starRating("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/SimpleReview.txt"));
    System.out.println("Fake Review:  " + Review.fakeReview("CSA_216_ConsumerLab_Starter/ConsumerLab_Code/SimpleReview.txt"));

  }
}