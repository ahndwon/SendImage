# SendImage
안드로이드로부터 AWS Storage로 이미지를 전송하고 겟하는 앱 (서버 필요)


## @POST IMAGE 

안드로이드 갤러리 -> 이미지 선택 -> REST API를 통해 SignedURL 얻음 -> SignedURL로 이미지 전송 -> aws storage에 이미지 저장

## @GET IMAGE

파일명 API로 전송 -> Redirected된 URL를 GLIDE를 통해 이미지 띄움
