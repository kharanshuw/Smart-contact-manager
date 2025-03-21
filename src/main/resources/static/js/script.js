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

function toggleTheme() {
    const newTheme = currentTheme === "light" ? "dark" : "light";

    document.querySelector('html').classList.remove(currentTheme);

    document.querySelector('html').classList.add(newTheme);

    localStorage.setItem("theme", newTheme);

    currentTheme = newTheme;

    updateButtonText();
}


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



document.addEventListener(
    "DOMContentLoaded", () => {
        const themeButton = document.querySelector("#theme_change_button");

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