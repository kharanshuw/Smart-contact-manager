console.log("contacts js loaded");



// Set the modal menu element
// Fetches the modal element from the DOM with the ID "view_contact_model"
const viewContactModel = document.getElementById('view_contact_model');

// Define options for the modal with default values
const options = {
    placement: 'bottom-right',// Position the modal at the bottom-right of the screen
    backdrop: 'dynamic', // Enable a dynamic backdrop for the modal
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',// Define custom backdrop classes for styling
    closable: true,// Make the modal closable
    onHide: () => {// Callback function executed when the modal is hidden
        console.log('modal is hidden');
    },
    onShow: () => {// Callback function executed when the modal is shown
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// Define instance options to customize modal behavior further
const instanceOptions = {
    id: 'view_contact_model', // Unique ID for the modal instance
    override: true // Allows overriding any existing modal instance with the same ID
};


// Create a new modal instance using the modal element, options, and instance options
const contactModal = new Modal(viewContactModel, options, instanceOptions);


/**
 * Function to show the modal
 * Calls the `show()` method on the `contactModal` instance to display the modal
 */
function showContactModel() {
    contactModal.show();
}

/**
 * Function to hide the modal
 * Calls the `hide()` method on the `contactModal` instance to hide the modal
 */
function hideContactModel() {
    contactModal.hide();
}



/**
 * Async function to load contact details from the backend
 * Fetches contact details by making an HTTP GET request to the provided API endpoint.
 *
 * @param {number} id - The ID of the contact to retrieve
 * @returns {Object} - The contact details as a JSON object, if successfully retrieved
 */
async function loadcontactdetails(id) {
    console.log("loading data of contact id : " + id);

    try {
        let response = await fetch(`http://localhost:8080/api/contact/${id}`);

        if (!response.ok) {
            console.error(`HTTP error! in loadcontactdetails Status: ${response.status}`)
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        let data = await response.json();

        console.log("printing data of contact ", data);

        let contact_name = document.querySelector('#contact_name');

        if (!contact_name) {
            console.error("contact_name not found ");
        }

        let contact_email = document.querySelector('#contact_email');

        if (!contact_email) {
            console.error("email not found ");
        }


        let contact_address = document.querySelector('#contact_address');

        if (!contact_address) {
            console.error("email not found ");
        }

        let description = document.querySelector('#description');

        if (!description) {
            console.error("description not found ");
        }



        //let cloudinaryImagename = document.querySelector('#cloudinaryImagename');

        let phoneNumber = document.querySelector("#phoneNumber");

        if (!phoneNumber) {
            console.error("phoneNumber not found ");
        }

        let picture = document.querySelector("#picture");


        let fevorite = document.querySelector("#fevorite");



        let facebookLink = document.querySelector("#facebookLink");


        if (!facebookLink) {
            console.error("facebookLink not found ");
        }

        let instagramLink = document.querySelector("#instagramLink");

        if (!instagramLink) {
            console.error("instagramLink not found ");
        }


        instagramLink.setAttribute("href", data.instagramLink);

        instagramLink.innerHTML = data.instagramLink || "N/A";

        facebookLink.innerHTML = data.facebookLink || "N/A";

        facebookLink.setAttribute("href", data.facebookLink);

        if (data.fevorite) {
            fevorite.innerHTML = "<i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i><i class='fas fa-star text-yellow-400'></i>";
        }
        else {
            fevorite.innerHTML = "Not Favorite Contact";
        }


        contact_name.innerHTML = data.name || "N/A";

        contact_email.innerHTML = data.email || "N/A";

        contact_address.innerHTML = data.address || "N/A";

        description.innerHTML = data.description || "N/A";

        //  cloudinaryImagename.innerHTML = data.cloudinaryImagename;

        phoneNumber.innerHTML = data.phoneNumber || "N/A";

        picture.setAttribute("src", data.picture);

        showContactModel();

        return data;
    } catch (error) {
        console.log("printing error " + error);
    }

}



async function deletecontact(id) {
    console.log("deletecontact called");

    console.log("id recived : " + id);

    try {

        let csrfToken  = await getcsrftoken();

        if(!csrfToken || !csrfToken.token)
        {
            console.error("CSRF token is invalid or not found");
            throw new Error("CSRF token is invalid or not found");
        }

        let token = csrfToken.token;

        console.log("token recived in deletecontact "+token);

        let response = await fetch(`http://localhost:8080/api/contact/delete/${id}`, {
            method: 'DELETE',
            headers : {
                'Content-Type':'application/json',
                'X-CSRF-TOKEN':token,
            },
        });

        if (!response.ok) {
            console.error(`HTTP error! Status: ${response.status}`)
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Log success message
        console.log("Contact deleted successfully");


        // Redirect to another page
        window.location.href = "/user/contacts/view";
    } catch (error) {
        console.error("error occured in deletecontact" + error);
    }

}


async function getcsrftoken() {
    console.log("getcsrftoken function called");

    try {
        let response = await fetch("http://localhost:8080/api/csrf-token");

        if (!response.ok) {
            console.error(`HTTP error! Status: ${response.status}`)
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        let data = await response.json();

        console.log("printing data of csrf token ", data);

        return data;

    } catch (error) {
        console.error("error occured in getcsrftoken")
        return null;
    }
}