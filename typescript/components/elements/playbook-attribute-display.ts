export class PlaybookAttributeDisplayElementComponent extends HTMLElement {
	constructor() {
		super();

		// Attach a shadow root to the element.
		const shadowRoot = this.attachShadow({ mode: "open" });

		// Define the inner structure and styles of the component.

		shadowRoot.innerHTML = `
            <style>
              figure {
                display:flex;
                flex-direction:row;
                justify-content: space-between;
                align-items:center;
                padding: 0.25rem 0 0;
                margin: 0.5rem 0 0;
                font-weight: bolder;
              }

              figure span {
                display: inline-block;
                border: 1px solid black;
                border-bottom: none;
                padding: 0.25rem 0.5rem;
                border-radius: 50% 50% 0 0 ;
              }

              hr {
                margin: 0 0 0.25rem;
                padding: 0;
              }

            </style>
            <figure class="header">
              <figcaption></figcaption>
              <span></span>
            </figure>
            <hr />
            <div class='children'><slot></slot></div>
        `;
	}

	connectedCallback() {
		// Update the content when the component is attached to the DOM.
		this.updateContent();
	}

	static get observedAttributes() {
		return ["label", "value"];
	}

	attributeChangedCallback(name: string, oldValue: string, newValue: string) {
		// Update the content when attributes change.
		this.updateContent();
	}

	updateContent() {
		const label = this.getAttribute("label") || "";
		this.shadowRoot.querySelector("figcaption").textContent = label;

		const value = this.getAttribute("value") || "";
		this.shadowRoot.querySelector("figure span").textContent = value;
	}
}

// Define the new element
customElements.define("fbtf-element-playbook-attribute-display", PlaybookAttributeDisplayElementComponent);
