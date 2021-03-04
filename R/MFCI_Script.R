#install.packages('rJava')
#install.packages('purrr')
#library(purrr)
#library(rJava)

#' @export
get_mfci<-function(transaction_list_filepath,items_list_filepath,including_absence,top_k,correlation_type,
                   continuity_correction,correlation_threshold,singleton){
  library(purrr)
  library(rJava)
  .jinit(".")
  .jaddClassPath("MFCI.jar")
  cs<- .jnew("processing.CorrelationSearch")
  t<-.jnew("java.lang.String", transaction_list_filepath)
  i<-.jnew("java.lang.String", items_list_filepath)
  pathname<-file(items_list_filepath,open="r");
  n<- length(readLines(pathname));
  closeAllConnections()
  r<-seq(0,n-1)
  ia<-.jnew("java.lang.Boolean", including_absence)
  ar<-.jarray(map(r,function(x) .jnew('java.lang.String',toString(x))),contents.class = "java/lang/String" )
  .jcall(cs,"V","initialData",i,t,ia)

  Integer <- J("java.lang.Integer")
  topk<-.jnew("java/lang/Integer", as.integer(top_k))
  cor<-.jnew("java.lang.String", correlation_type)
  Double<- J("java.lang.Double")
  cont<-Double$parseDouble(continuity_correction)
  thresh<-Double$parseDouble(correlation_threshold)
  singleK<-.jnew("java.lang.Boolean",singleton)
  .jcall(cs,"Ljava/lang/String;", 'ShowMFCI', ar,topk,cor,cont,thresh,singleK)
}

#' @export
get_pairs<-function(transaction_list_filepath,items_list_filepath,itemset,including_absence,top_k,correlation_type,
                   continuity_correction,correlation_threshold,detail)
  {
  library(purrr)
  library(rJava)
  .jinit(".")
  .jaddClassPath("MFCI.jar")
  cs<- .jnew("processing.CorrelationSearch")
  t<-.jnew("java.lang.String", transaction_list_filepath)
  i<-.jnew("java.lang.String", items_list_filepath)
  ia<-.jnew("java.lang.Boolean", including_absence)
  .jcall(cs,"V","initialData",i,t,ia)
  search_type<-.jnew("java.lang.String","Token-ring Search")
  use_detail<-.jnew("java.lang.Boolean", detail)
  Integer <- J("java.lang.Integer")
  topk<-as.integer(top_k)
  cor<-.jnew("java.lang.String", correlation_type)
  Double<- J("java.lang.Double")
  cont<-Double$parseDouble(continuity_correction)
  thresh<-Double$parseDouble(correlation_threshold)
  itemset_ar<-.jarray(map(unlist(strsplit(itemset,",")),function(x) .jnew('java.lang.String',toString(x))),contents.class = "java/lang/String")
  .jcall(cs,'S','getTopKPairInformation',itemset_ar,topk,search_type,cor,cont,thresh,use_detail)

}

#' @export
mfci_setup<- function(){
  trans<- readline("What is the name of the transaction file? ")
  it<- readline("What is the name of the items file? ")
  ktop<- readline("What would you like to set as the Top-K? ")
  corr_thresh<-readline("What would you like to set the Correlation Threshold as? ")
  corr_type<- readline("What type of Measure what you like? (Choices: BCPNN, Leverage, Likelihood Ratio) ")
  ktop<-as.integer(ktop)

  print("Number immediately following count down from 6 outputs number of transaction list rows.")
  gm<-get_mfci(trans,it,FALSE,ktop,corr_type,"0.5",corr_thresh,FALSE)
  return(gm)
}


#' @export
mfci<-function(){
  cat(mfci_setup())
}


#' @export
pairs_setup<-function(){
  trans<- readline("What is the name of the transaction file? ")
  it<- readline("What is the name of the items file? ")
  itmset<-readline("What is the list of item IDs you'd like to Analyze? ")
  corr_type<- readline("What type of Measure would you like? (Choices: BCPNN, Leverage, Likelihood Ratio) ")
  ktop<- readline("What would you like to set as the Top-K? ")
  corr_thresh<-readline("What would you like to set the Correlation Threshold as? ")
  det<- readline("Would you like the ouput formatted by item ID or actual item? (TRUE for ItemID, FALSE for Item) ")

  print("Number immediately following count down from 6 outputs number of transaction list rows.")
  return(get_pairs(trans,it,itmset,FALSE,ktop,corr_type,"0.5",corr_thresh,det))

}

#' @export
pairs<-function(){
  cat(pairs_setup())
  }



