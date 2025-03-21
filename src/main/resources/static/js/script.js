console.log("Theme Switcher Script Loaded");
console.log("theme stored in localStorage is ", localStorage.getItem('theme'));

//This line sets a variable currentTheme to the value of the theme stored in local storage.
//If no value is found in local storage, it defaults to "light".
let currentTheme = localStorage.getItem("theme") || "light";


//This line selects the <html> element in the document and sets its class attribute to an empty string.
//This effectively removes any existing classes from the <html> element.
document.querySelector('html').setAttribute('class', "");

// Apply the initial theme
document.querySelector('html').classList.add(currentTheme);

/**
 * Toggle the theme between light and dark.
 *
 * This function is responsible for flipping the UI's theme from light to dark
 * or vice versa. It does this by removing the old theme class, adding the new
 * one, and updating the value stored in localStorage.
 *
 * The function is called from the theme button click event.
 */
function toggleTheme() {
    const newTheme = currentTheme === "light" ? "dark" : "light";

    document.querySelector('html').classList.remove(currentTheme);

    document.querySelector('html').classList.add(newTheme);

    localStorage.setItem("theme", newTheme);

    currentTheme = newTheme;

    updateButtonText();
}


/**
 * Update the text of the theme toggle button.
 *
 * This function changes the text content of the button used to toggle themes
 * between "Dark Mode" and "Light Mode" based on the current theme. It selects
 * the button with the ID "theme_change_button_span" and updates its text
 * accordingly. If the button cannot be found, no action is taken.
 */
function updateButtonText() {
    const themeButton = document.querySelector("#theme_change_button_span");

    if (themeButton) {
        //console.log("themeButton selected successfully ", themeButton);

        if (currentTheme === "light") {
            themeButton.textContent = "Dark Mode";
        } else {
            themeButton.textContent = "Light Mode";
        }
    }
}


/**
 * This code snippet is adding an event listener to the document object, 
 * listening for the DOMContentLoaded event. 
 * This event is fired when the initial HTML document has been completely loaded and parsed, without waiting for stylesheets, images, and subframes to finish loading.
 * When the DOMContentLoaded event is fired, the code inside the callback function is executed. 
 */
document.addEventListener(
    "DOMContentLoaded", () => {

        const themeButton = document.querySelector("#theme_change_button");

        /**
         * If the element is found, it adds an event listener to the element, 
         * listening for a click event. When the element is clicked, the toggleTheme function is called.
         */
        if (themeButton) {
            //console.log("themeButton successfully selected in addEventListener", themeButton);

            themeButton.addEventListener("click", toggleTheme);

            updateButtonText();

        }

        else {
            console.log("Theme toggle button not found. Make sure you have an element with id='themeToggle' in your HTML");
        }
    }
)