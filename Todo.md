### Initial Todo: ###
  1. start as service **(used BroadcastReceiver with SMS\_RECEIVED Filter)**
  1. read SMS **(done)**
    * trigger on SMS received
  1. parse for "key" -> action **(done)**
    * save sender  **(done)**
    * get position **(done)**
  1. return SMS with position **(done)**
    * compile SMS -> send to sender
  1. delete SMS **(not needed, received/sent sms' will not show up)**


### Additional Todo: ###
  1. Review existing code
  1. Refine/-factor existing code
  1. Test on real device
  1. Additional app which shows location on (Google) maps?