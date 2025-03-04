export class CenterfoldLayoutComponent extends HTMLElement {
	constructor() {
		super();

		// Attach a shadow root to the element.
		const shadowRoot = this.attachShadow({ mode: "open" });

		// Define the inner structure and styles of the component.
		shadowRoot.innerHTML = `
            <style>
                .content {
                    display:flex;
                    flex-direction:column;
                    justify-content: center;
                    align-items:center;
                }
            </style>
            <div class="content"><slot></slot></div>
        `;
	}

	connectedCallback() {
		// Update the content when the component is attached to the DOM.
		this.updateContent();
	}

	static get observedAttributes() {
		return ["title", "description"];
	}

	attributeChangedCallback(name: string, oldValue: string, newValue: string) {
		// Update the content when attributes change.
		this.updateContent();
	}

	updateContent() {
		const title = this.getAttribute("title") || "";
		const description = this.getAttribute("description") || "";
		this.shadowRoot.querySelector(".title").textContent = title;
		this.shadowRoot.querySelector(".description").textContent = description;
	}
}

// Define the new element
customElements.define("fbtf-layout-centerfold", CenterfoldLayoutComponent);
