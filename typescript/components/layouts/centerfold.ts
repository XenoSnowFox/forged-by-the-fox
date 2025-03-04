export class CenterfoldLayoutComponent extends HTMLElement {
	constructor() {
		super();
		const shadowRoot = this.attachShadow({ mode: "open" });
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
}

customElements.define("fbtf-layout-centerfold", CenterfoldLayoutComponent);
