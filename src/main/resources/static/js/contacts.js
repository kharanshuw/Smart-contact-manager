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
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        let data = await response.json();

        console.log("printing data of contact ", data);

        let contact_name = document.querySelector('#contact_name');

        let contact_email = document.querySelector('#contact_email');

        let contact_address = document.querySelector('#contact_address');

        let description = document.querySelector('#description');

        let cloudinaryImagename = document.querySelector('#cloudinaryImagename');

        let phoneNumber = document.querySelector("#phoneNumber");

        let picture = document.querySelector("#picture");

        let fevorite = document.querySelector("#fevorite");

        contact_name.innerHTML = data.name;

        contact_email.innerHTML = data.email;

        contact_address.innerHTML = data.address;

        description.innerHTML = data.description;

        cloudinaryImagename.innerHTML = data.cloudinaryImagename;

        phoneNumber.innerHTML = data.phoneNumber;

        picture.setAttribute("href",data.picture);

        fevorite.innerHTML = data.fevorite;


        showContactModel();

        return data;
    } catch (error) {
        console.log("printing error " + error)
    }

}