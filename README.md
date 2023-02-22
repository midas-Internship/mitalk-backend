# mitalk-backend=
```
.
├── Dockerfile
├── README.md
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── src
    ├── main
    │   ├── kotlin
    │   │   └── com
    │   │       └── example
    │   │           └── mitalk
    │   │               ├── MitalkApplication.kt
    │   │               ├── domain
    │   │               │   ├── admin
    │   │               │   │   ├── domain
    │   │               │   │   │   ├── entity
    │   │               │   │   │   │   └── Admin.kt
    │   │               │   │   │   └── repository
    │   │               │   │   │       └── AdminRepository.kt
    │   │               │   │   ├── exception
    │   │               │   │   │   ├── AdminNotFoundException.kt
    │   │               │   │   │   └── AlreadyExistsQuestionException.kt
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── AdminController.kt
    │   │               │   │   │   └── data
    │   │               │   │   │       ├── request
    │   │               │   │   │       │   ├── CreateCounsellorRequest.kt
    │   │               │   │   │       │   ├── CreateQuestionRequest.kt
    │   │               │   │   │       │   ├── DeleteCounsellorRequest.kt
    │   │               │   │   │       │   └── UpdateQuestionRequest.kt
    │   │               │   │   │       └── response
    │   │               │   │   │           ├── CreateCounsellorResponse.kt
    │   │               │   │   │           ├── FindAllCounsellorResponse.kt
    │   │               │   │   │           ├── FindQuestionResponse.kt
    │   │               │   │   │           ├── GetCustomerListResponse.kt
    │   │               │   │   │           ├── GetRecordListRequest.kt
    │   │               │   │   │           ├── GetStatisticsDetailResponse.kt
    │   │               │   │   │           ├── GetStatisticsResponse.kt
    │   │               │   │   │           └── StatisticReviewElement.kt
    │   │               │   │   └── service
    │   │               │   │       ├── AdminResetService.kt
    │   │               │   │       ├── CreateCounsellorService.kt
    │   │               │   │       ├── CreateQuestionService.kt
    │   │               │   │       ├── DeleteCounsellorService.kt
    │   │               │   │       ├── FindAllCounsellorService.kt
    │   │               │   │       ├── FindAllQuestionService.kt
    │   │               │   │       ├── GetCustomerListService.kt
    │   │               │   │       ├── GetRecordListService.kt
    │   │               │   │       ├── GetStatisticsDetailService.kt
    │   │               │   │       ├── GetStatisticsService.kt
    │   │               │   │       └── UpdateQuestionService.kt
    │   │               │   ├── auth
    │   │               │   │   ├── domain
    │   │               │   │   │   ├── Role.kt
    │   │               │   │   │   ├── entity
    │   │               │   │   │   │   └── RefreshToken.kt
    │   │               │   │   │   └── repository
    │   │               │   │   │       └── RefreshTokenRepository.kt
    │   │               │   │   ├── exception
    │   │               │   │   │   ├── ExpiredRefreshTokenException.kt
    │   │               │   │   │   └── OfficialsNotFoundException.kt
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── AuthController.kt
    │   │               │   │   │   └── data
    │   │               │   │   │       ├── dto
    │   │               │   │   │       │   └── OfficeTokenDto.kt
    │   │               │   │   │       ├── request
    │   │               │   │   │       │   └── SignInOfficeRequest.kt
    │   │               │   │   │       └── response
    │   │               │   │   │           ├── NewRefreshTokenResponse.kt
    │   │               │   │   │           └── SignInOfficeResponse.kt
    │   │               │   │   └── service
    │   │               │   │       ├── GetNewRefreshTokenService.kt
    │   │               │   │       └── impl
    │   │               │   │           ├── GetNewRefreshTokenServiceImpl.kt
    │   │               │   │           └── SignInOfficeService.kt
    │   │               │   ├── counsellor
    │   │               │   │   ├── domain
    │   │               │   │   │   ├── entity
    │   │               │   │   │   │   └── Counsellor.kt
    │   │               │   │   │   └── repository
    │   │               │   │   │       └── CounsellorRepository.kt
    │   │               │   │   ├── exception
    │   │               │   │   │   └── CounsellorNotFoundException.kt
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── CounsellorController.kt
    │   │               │   │   │   └── data
    │   │               │   │   │       ├── request
    │   │               │   │   │       │   └── ActivityStatusRequest.kt
    │   │               │   │   │       └── response
    │   │               │   │   │           ├── ActivityStatusResponse.kt
    │   │               │   │   │           └── FindActivityStatusResponse.kt
    │   │               │   │   └── service
    │   │               │   │       ├── ActiveStatusService.kt
    │   │               │   │       └── FindActivityStatusService.kt
    │   │               │   ├── customer
    │   │               │   │   ├── domain
    │   │               │   │   │   ├── ReviewItem.kt
    │   │               │   │   │   ├── entity
    │   │               │   │   │   │   ├── Customer.kt
    │   │               │   │   │   │   ├── CustomerInfo.kt
    │   │               │   │   │   │   ├── CustomerQueue.kt
    │   │               │   │   │   │   ├── Question.kt
    │   │               │   │   │   │   ├── Review.kt
    │   │               │   │   │   │   └── ReviewElement.kt
    │   │               │   │   │   └── repository
    │   │               │   │   │       ├── CustomerInfoRepository.kt
    │   │               │   │   │       ├── CustomerRepository.kt
    │   │               │   │   │       ├── QuestionRepository.kt
    │   │               │   │   │       ├── ReviewElementRepository.kt
    │   │               │   │   │       └── ReviewRepository.kt
    │   │               │   │   ├── exception
    │   │               │   │   │   └── CustomerNotFoundException.kt
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── CustomerController.kt
    │   │               │   │   │   └── data
    │   │               │   │   │       ├── request
    │   │               │   │   │       │   ├── ReviewRequest.kt
    │   │               │   │   │       │   └── SignInRequest.kt
    │   │               │   │   │       └── response
    │   │               │   │   │           ├── CurrentStatusResponse.kt
    │   │               │   │   │           ├── QuestionListResponse.kt
    │   │               │   │   │           └── SignInResponseDto.kt
    │   │               │   │   ├── service
    │   │               │   │   │   ├── CurrentStatusService.kt
    │   │               │   │   │   ├── FindAllQuestionListService.kt
    │   │               │   │   │   ├── ReviewService.kt
    │   │               │   │   │   └── SignInService.kt
    │   │               │   │   └── util
    │   │               │   │       └── CustomerGeneratedUtil.kt
    │   │               │   ├── email
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── data
    │   │               │   │   │   │   └── dto
    │   │               │   │   │   │       ├── EmailSentDto.kt
    │   │               │   │   │   │       └── TemplateDataDto.kt
    │   │               │   │   │   └── domain
    │   │               │   │   │       ├── DocumentAllowed.kt
    │   │               │   │   │       ├── ImageAllowed.kt
    │   │               │   │   │       └── VideoAllowed.kt
    │   │               │   │   └── service
    │   │               │   │       └── MailSenderService.kt
    │   │               │   ├── image
    │   │               │   │   ├── exception
    │   │               │   │   │   └── MaximerFileSizeException.kt
    │   │               │   │   ├── presentation
    │   │               │   │   │   ├── S3Controller.kt
    │   │               │   │   │   └── data
    │   │               │   │   │       ├── FileDto.kt
    │   │               │   │   │       └── UploadFileDto.kt
    │   │               │   │   └── service
    │   │               │   │       └── UploadFileService.kt
    │   │               │   └── record
    │   │               │       ├── domain
    │   │               │       │   ├── entity
    │   │               │       │   │   ├── CounsellingType.kt
    │   │               │       │   │   └── Record.kt
    │   │               │       │   └── repository
    │   │               │       │       └── RecordRepository.kt
    │   │               │       ├── presentation
    │   │               │       │   ├── RecordController.kt
    │   │               │       │   └── data
    │   │               │       │       └── response
    │   │               │       │           ├── GetRecordDetailResponse.kt
    │   │               │       │           └── GetRecordResponse.kt
    │   │               │       └── service
    │   │               │           ├── GetRecordDetailService.kt
    │   │               │           └── GetRecordService.kt
    │   │               └── global
    │   │                   ├── annotation
    │   │                   │   └── Superclass.kt
    │   │                   ├── batch
    │   │                   │   └── Scheduler.kt
    │   │                   ├── bean
    │   │                   │   └── InitCustomerQueue.kt
    │   │                   ├── config
    │   │                   │   └── s3
    │   │                   │       └── S3Config.kt
    │   │                   ├── exception
    │   │                   │   ├── ErrorCode.kt
    │   │                   │   ├── ErrorResponse.kt
    │   │                   │   ├── exceptions
    │   │                   │   │   └── BasicException.kt
    │   │                   │   └── handler
    │   │                   │       └── GlobalExceptionHandler.kt
    │   │                   ├── redis
    │   │                   │   └── config
    │   │                   │       └── RedisConfig.kt
    │   │                   ├── security
    │   │                   │   ├── CustomAuthenticationEntryPoint.kt
    │   │                   │   ├── SecurityConfig.kt
    │   │                   │   ├── auth
    │   │                   │   │   ├── AdminDetailService.kt
    │   │                   │   │   ├── AdminDetails.kt
    │   │                   │   │   ├── CounsellorDetailService.kt
    │   │                   │   │   ├── CounsellorDetails.kt
    │   │                   │   │   ├── CustomerDetailService.kt
    │   │                   │   │   └── CustomerDetails.kt
    │   │                   │   ├── exception
    │   │                   │   │   ├── ExpiredTokenException.kt
    │   │                   │   │   └── InvalidTokenException.kt
    │   │                   │   ├── filter
    │   │                   │   │   ├── ExceptionFilter.kt
    │   │                   │   │   ├── FilterConfig.kt
    │   │                   │   │   └── JwtTokenFilter.kt
    │   │                   │   └── jwt
    │   │                   │       ├── JwtTokenProvider.kt
    │   │                   │       └── properties
    │   │                   │           └── JwtProperties.kt
    │   │                   ├── socket
    │   │                   │   ├── config
    │   │                   │   │   └── WebSocketConfig.kt
    │   │                   │   ├── handler
    │   │                   │   │   └── SocketHandler.kt
    │   │                   │   ├── message
    │   │                   │   │   ├── ChatMessage.kt
    │   │                   │   │   ├── CounsellingStartMessage.kt
    │   │                   │   │   ├── CurrentQueueMessage.kt
    │   │                   │   │   ├── EnterQueueSuccessMessage.kt
    │   │                   │   │   ├── QueueAlreadyFilledMessage.kt
    │   │                   │   │   ├── RoomBurstEventMessage.kt
    │   │                   │   │   └── element
    │   │                   │   │       ├── MessageType.kt
    │   │                   │   │       └── SystemMessage.kt
    │   │                   │   └── util
    │   │                   │       ├── MessageUtils.kt
    │   │                   │       └── SessionUtils.kt
    │   │                   └── util
    │   │                       └── UserUtil.kt
    │   └── resources
    │       └── application.yml
    └── test
        └── kotlin
            └── com
                └── example
                    └── mitalk
                        └── MitalkApplicationTests.kt
```
101 directories, 141 files
