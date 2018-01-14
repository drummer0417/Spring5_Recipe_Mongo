package nl.androidappfactory.recipe.controllers;

// public class ImageControllerTest {
//
// public static Byte[] IMAGE;
//
// @Mock
// ImageService imageService;
//
// @Mock
// RecipeService recipeService;
//
// ImageController controller;
//
// MockMvc mockMvc;
//
// @Before
// public void setUp() throws Exception {
// MockitoAnnotations.initMocks(this);
//
// controller = new ImageController(imageService, recipeService);
// mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//
// String testString = "here is the test String";
// Byte[] testBytes = new Byte[testString.length()];
//
// int i = 0;
// for (byte aByte : testString.getBytes()) {
// testBytes[i++] = aByte;
// }
//
// IMAGE = testBytes;
// }
//
// @Test
// public void getImageForm() throws Exception {
// // given
// RecipeCommand command = new RecipeCommand();
// command.setId("1");
//
// when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
//
// // when
// mockMvc.perform(get("/recipe/1/image"))
// .andExpect(status().isOk())
// .andExpect(model().attributeExists("recipe"));
//
// verify(recipeService, times(1)).findCommandById(anyString());
//
// }
//
// @Test
// public void handleImagePost() throws Exception {
//
// MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
// "Greetings from androidappfactory".getBytes());
//
// when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(new RecipeCommand()));
// when(imageService.saveImageFile(anyString(), any())).thenReturn(Mono.empty());
//
// mockMvc.perform(multipart("/recipe/1/image")
// .file(multipartFile))
// .andExpect(status().is3xxRedirection())
// .andExpect(header().string("Location", "/recipe/1/show"));
//
// verify(imageService, times(1)).saveImageFile(anyString(), any());
// }
//
// @Test
// public void getImageFromDB() throws Exception {
//
// // given
// RecipeCommand command = new RecipeCommand();
// command.setId("1");
//
// command.setImage(IMAGE);
//
// when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));
//
// // when
// MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
// .andExpect(status().isOk())
// .andReturn().getResponse();
//
// byte[] reponseBytes = response.getContentAsByteArray();
//
// assertEquals(IMAGE.length, reponseBytes.length);
// }
// }