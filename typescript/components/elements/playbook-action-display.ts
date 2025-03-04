export class PlaybookActionDisplayElementComponent extends HTMLElement {
	constructor() {
		super();

		// Attach a shadow root to the element.
		const shadowRoot = this.attachShadow({ mode: "open" });

		// Define the inner structure and styles of the component.
		shadowRoot.innerHTML = `
            <style>
              .content {
                display:flex;
                flex-direction:row;
                justify-content: space-between;
                align-items:center;
                padding: 0.125rem 0;
              }

              .pips {
                display: flex;
                flex-direction: row;
                justify-content: flex-end;
                align-items:center;
                gap: 0.25rem;
              }

              svg {
                height: 1rem;
                opacity: 0.125;
              }

              svg.active {
                opacity: 1;
              }

            </style>
            <div class="content">
              <label><slot></slot></label>
              <div class='pips'>
                <svg viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M6 -3.49691e-07L14 8L6 16L4 16L4 -4.37114e-07L6 -3.49691e-07Z" />
                </svg>
                <svg viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M6 -3.49691e-07L14 8L6 16L4 16L4 -4.37114e-07L6 -3.49691e-07Z" />
                </svg>
                <svg viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                    <path d="M6 -3.49691e-07L14 8L6 16L4 16L4 -4.37114e-07L6 -3.49691e-07Z" />
                </svg>
              </div>
            </div>
        `;
	}

	connectedCallback() {
		// Update the content when the component is attached to the DOM.
		this.updateContent();
	}

	static get observedAttributes() {
		return ["value"];
	}

	attributeChangedCallback(name: string, oldValue: string, newValue: string) {
		// Update the content when attributes change.
		this.updateContent();
	}

	updateContent() {
		const value = parseInt(this.getAttribute("value") || "");
		const pips = this.shadowRoot.querySelectorAll(".pips svg");
		for (let i = 0; i < pips.length; i++) {
			if (value > i) {
				pips[i].classList.add("active");
			} else {
				pips[i].classList.remove("active");
			}
		}
	}
}

// Define the new element
customElements.define("fbtf-element-playbook-action-display", PlaybookActionDisplayElementComponent);
