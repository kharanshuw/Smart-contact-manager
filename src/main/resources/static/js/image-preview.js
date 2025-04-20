console.log("image preview script loaded");


// Adds an event listener to the element with the ID "profileImage".
// The event listener is triggered whenever a user selects a file (e.g., an image) from their device.
document.querySelector("#profileImage").addEventListener("change", function (event) {


    console.log("getting file for preview...");

    // Retrieves the first file selected by the user from the file input element.

    let file = event.target.files[0];

    // Creates a new FileReader instance to asynchronously read the file's content.
    let reader = new FileReader();

    // Defines the function to be executed once the FileReader successfully reads the file content.
    reader.onload = function () {

        // Sets the 'src' attribute of the element with ID "upload_image_preview" to the file's content (Base64 string).
        // This updates the source of an <img> tag to display the preview of the uploaded image.

        document.querySelector("#upload_image_preview").setAttribute("src", reader.result);
    }

    // Starts reading the content of the selected file.
    // The result (Base64 string) will trigger the 'onload' event once reading is complete.

    reader.readAsDataURL(file)
})




/* Functionality Summary:
1. **Purpose**: This script enables users to preview an image they have uploaded in an input field. The preview is displayed in an `img` tag with the ID `upload_image_preview`.
2. **Core Components**:
   - The `FileReader` API is used to read the file selected by the user.
   - The `onload` event is triggered once the file content is successfully read, updating the `src` attribute of the `<img>` tag.
3. **How It Works**:
   - The `<input>` element with the ID `profileImage` listens for the `change` event (file selection).
   - The script reads the file using `FileReader` and converts it to a Base64 string, which the browser can use as an image source.
   */
