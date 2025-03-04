/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./typescript/components/elements/playbook-action-display.ts":
/*!*******************************************************************!*\
  !*** ./typescript/components/elements/playbook-action-display.ts ***!
  \*******************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.PlaybookActionDisplayElementComponent = void 0;
class PlaybookActionDisplayElementComponent extends HTMLElement {
    constructor() {
        super();
        const shadowRoot = this.attachShadow({ mode: "open" });
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
        this.updateContent();
    }
    static get observedAttributes() {
        return ["value"];
    }
    attributeChangedCallback(name, oldValue, newValue) {
        this.updateContent();
    }
    updateContent() {
        const value = parseInt(this.getAttribute("value") || "");
        const pips = this.shadowRoot.querySelectorAll(".pips svg");
        for (let i = 0; i < pips.length; i++) {
            if (value > i) {
                pips[i].classList.add("active");
            }
            else {
                pips[i].classList.remove("active");
            }
        }
    }
}
exports.PlaybookActionDisplayElementComponent = PlaybookActionDisplayElementComponent;
customElements.define("fbtf-element-playbook-action-display", PlaybookActionDisplayElementComponent);


/***/ }),

/***/ "./typescript/components/elements/playbook-attribute-display.ts":
/*!**********************************************************************!*\
  !*** ./typescript/components/elements/playbook-attribute-display.ts ***!
  \**********************************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.PlaybookAttributeDisplayElementComponent = void 0;
class PlaybookAttributeDisplayElementComponent extends HTMLElement {
    constructor() {
        super();
        const shadowRoot = this.attachShadow({ mode: "open" });
        shadowRoot.innerHTML = `
            <style>
              figure {
                display:flex;
                flex-direction:row;
                justify-content: space-between;
                align-items:center;
                padding: 0.25rem 0;
                margin: 0.5rem 0 0;
                font-weight: bolder;
              }

              figure span {
                display: inline-block;
                border: 1px solid black;
                padding: 0.25rem;
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
        this.updateContent();
    }
    static get observedAttributes() {
        return ["label", "value"];
    }
    attributeChangedCallback(name, oldValue, newValue) {
        this.updateContent();
    }
    updateContent() {
        const label = this.getAttribute("label") || "";
        this.shadowRoot.querySelector("figcaption").textContent = label;
        const value = this.getAttribute("value") || "";
        this.shadowRoot.querySelector("figure span").textContent = value;
    }
}
exports.PlaybookAttributeDisplayElementComponent = PlaybookAttributeDisplayElementComponent;
customElements.define("fbtf-element-playbook-attribute-display", PlaybookAttributeDisplayElementComponent);


/***/ }),

/***/ "./typescript/components/layouts/centerfold.ts":
/*!*****************************************************!*\
  !*** ./typescript/components/layouts/centerfold.ts ***!
  \*****************************************************/
/***/ ((__unused_webpack_module, exports) => {


Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.CenterfoldLayoutComponent = void 0;
class CenterfoldLayoutComponent extends HTMLElement {
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
exports.CenterfoldLayoutComponent = CenterfoldLayoutComponent;
customElements.define("fbtf-layout-centerfold", CenterfoldLayoutComponent);


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry needs to be wrapped in an IIFE because it needs to be isolated against other modules in the chunk.
(() => {
var exports = __webpack_exports__;
/*!**************************************!*\
  !*** ./typescript/web-components.ts ***!
  \**************************************/

Object.defineProperty(exports, "__esModule", ({ value: true }));
exports.CenterfoldLayoutComponent = exports.PlaybookAttributeDisplayElementComponent = exports.PlaybookActionDisplayElementComponent = void 0;
var playbook_action_display_1 = __webpack_require__(/*! ./components/elements/playbook-action-display */ "./typescript/components/elements/playbook-action-display.ts");
Object.defineProperty(exports, "PlaybookActionDisplayElementComponent", ({ enumerable: true, get: function () { return playbook_action_display_1.PlaybookActionDisplayElementComponent; } }));
var playbook_attribute_display_1 = __webpack_require__(/*! ./components/elements/playbook-attribute-display */ "./typescript/components/elements/playbook-attribute-display.ts");
Object.defineProperty(exports, "PlaybookAttributeDisplayElementComponent", ({ enumerable: true, get: function () { return playbook_attribute_display_1.PlaybookAttributeDisplayElementComponent; } }));
var centerfold_1 = __webpack_require__(/*! ./components/layouts/centerfold */ "./typescript/components/layouts/centerfold.ts");
Object.defineProperty(exports, "CenterfoldLayoutComponent", ({ enumerable: true, get: function () { return centerfold_1.CenterfoldLayoutComponent; } }));

})();

/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoid2ViLWNvbXBvbmVudHMuanMiLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7OztBQUFhO0FBQ2IsOENBQTZDLEVBQUUsYUFBYSxFQUFDO0FBQzdELDZDQUE2QztBQUM3QztBQUNBO0FBQ0E7QUFDQSwrQ0FBK0MsY0FBYztBQUM3RDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSx3QkFBd0IsaUJBQWlCO0FBQ3pDO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLDZDQUE2QztBQUM3Qzs7Ozs7Ozs7Ozs7QUMxRWE7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsZ0RBQWdEO0FBQ2hEO0FBQ0E7QUFDQTtBQUNBLCtDQUErQyxjQUFjO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGdEQUFnRDtBQUNoRDs7Ozs7Ozs7Ozs7QUN4RGE7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsaUNBQWlDO0FBQ2pDO0FBQ0E7QUFDQTtBQUNBLCtDQUErQyxjQUFjO0FBQzdEO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsaUNBQWlDO0FBQ2pDOzs7Ozs7O1VDckJBO1VBQ0E7O1VBRUE7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7O1VBRUE7VUFDQTs7VUFFQTtVQUNBO1VBQ0E7Ozs7Ozs7Ozs7QUN0QmE7QUFDYiw4Q0FBNkMsRUFBRSxhQUFhLEVBQUM7QUFDN0QsaUNBQWlDLEdBQUcsZ0RBQWdELEdBQUcsNkNBQTZDO0FBQ3BJLGdDQUFnQyxtQkFBTyxDQUFDLGtIQUErQztBQUN2Rix5RUFBd0UsRUFBRSxxQ0FBcUMsMkVBQTJFLEVBQUM7QUFDM0wsbUNBQW1DLG1CQUFPLENBQUMsd0hBQWtEO0FBQzdGLDRFQUEyRSxFQUFFLHFDQUFxQyxpRkFBaUYsRUFBQztBQUNwTSxtQkFBbUIsbUJBQU8sQ0FBQyxzRkFBaUM7QUFDNUQsNkRBQTRELEVBQUUscUNBQXFDLGtEQUFrRCxFQUFDIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vZm9yZ2VkLWJ5LXRoZS1mb3gvLi90eXBlc2NyaXB0L2NvbXBvbmVudHMvZWxlbWVudHMvcGxheWJvb2stYWN0aW9uLWRpc3BsYXkudHMiLCJ3ZWJwYWNrOi8vZm9yZ2VkLWJ5LXRoZS1mb3gvLi90eXBlc2NyaXB0L2NvbXBvbmVudHMvZWxlbWVudHMvcGxheWJvb2stYXR0cmlidXRlLWRpc3BsYXkudHMiLCJ3ZWJwYWNrOi8vZm9yZ2VkLWJ5LXRoZS1mb3gvLi90eXBlc2NyaXB0L2NvbXBvbmVudHMvbGF5b3V0cy9jZW50ZXJmb2xkLnRzIiwid2VicGFjazovL2ZvcmdlZC1ieS10aGUtZm94L3dlYnBhY2svYm9vdHN0cmFwIiwid2VicGFjazovL2ZvcmdlZC1ieS10aGUtZm94Ly4vdHlwZXNjcmlwdC93ZWItY29tcG9uZW50cy50cyJdLCJzb3VyY2VzQ29udGVudCI6WyJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5leHBvcnRzLlBsYXlib29rQWN0aW9uRGlzcGxheUVsZW1lbnRDb21wb25lbnQgPSB2b2lkIDA7XHJcbmNsYXNzIFBsYXlib29rQWN0aW9uRGlzcGxheUVsZW1lbnRDb21wb25lbnQgZXh0ZW5kcyBIVE1MRWxlbWVudCB7XHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgICAgICBzdXBlcigpO1xyXG4gICAgICAgIGNvbnN0IHNoYWRvd1Jvb3QgPSB0aGlzLmF0dGFjaFNoYWRvdyh7IG1vZGU6IFwib3BlblwiIH0pO1xyXG4gICAgICAgIHNoYWRvd1Jvb3QuaW5uZXJIVE1MID0gYFxuICAgICAgICAgICAgPHN0eWxlPlxuICAgICAgICAgICAgICAuY29udGVudCB7XG4gICAgICAgICAgICAgICAgZGlzcGxheTpmbGV4O1xuICAgICAgICAgICAgICAgIGZsZXgtZGlyZWN0aW9uOnJvdztcbiAgICAgICAgICAgICAgICBqdXN0aWZ5LWNvbnRlbnQ6IHNwYWNlLWJldHdlZW47XG4gICAgICAgICAgICAgICAgYWxpZ24taXRlbXM6Y2VudGVyO1xuICAgICAgICAgICAgICAgIHBhZGRpbmc6IDAuMTI1cmVtIDA7XG4gICAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgICAucGlwcyB7XG4gICAgICAgICAgICAgICAgZGlzcGxheTogZmxleDtcbiAgICAgICAgICAgICAgICBmbGV4LWRpcmVjdGlvbjogcm93O1xuICAgICAgICAgICAgICAgIGp1c3RpZnktY29udGVudDogZmxleC1lbmQ7XG4gICAgICAgICAgICAgICAgYWxpZ24taXRlbXM6Y2VudGVyO1xuICAgICAgICAgICAgICAgIGdhcDogMC4yNXJlbTtcbiAgICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICAgIHN2ZyB7XG4gICAgICAgICAgICAgICAgaGVpZ2h0OiAxcmVtO1xuICAgICAgICAgICAgICAgIG9wYWNpdHk6IDAuMTI1O1xuICAgICAgICAgICAgICB9XG5cbiAgICAgICAgICAgICAgc3ZnLmFjdGl2ZSB7XG4gICAgICAgICAgICAgICAgb3BhY2l0eTogMTtcbiAgICAgICAgICAgICAgfVxuXG4gICAgICAgICAgICA8L3N0eWxlPlxuICAgICAgICAgICAgPGRpdiBjbGFzcz1cImNvbnRlbnRcIj5cbiAgICAgICAgICAgICAgPGxhYmVsPjxzbG90Pjwvc2xvdD48L2xhYmVsPlxuICAgICAgICAgICAgICA8ZGl2IGNsYXNzPSdwaXBzJz5cbiAgICAgICAgICAgICAgICA8c3ZnIHZpZXdCb3g9XCIwIDAgMTYgMTZcIiB4bWxucz1cImh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnXCI+XG4gICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9XCJNNiAtMy40OTY5MWUtMDdMMTQgOEw2IDE2TDQgMTZMNCAtNC4zNzExNGUtMDdMNiAtMy40OTY5MWUtMDdaXCIgLz5cbiAgICAgICAgICAgICAgICA8L3N2Zz5cbiAgICAgICAgICAgICAgICA8c3ZnIHZpZXdCb3g9XCIwIDAgMTYgMTZcIiB4bWxucz1cImh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnXCI+XG4gICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9XCJNNiAtMy40OTY5MWUtMDdMMTQgOEw2IDE2TDQgMTZMNCAtNC4zNzExNGUtMDdMNiAtMy40OTY5MWUtMDdaXCIgLz5cbiAgICAgICAgICAgICAgICA8L3N2Zz5cbiAgICAgICAgICAgICAgICA8c3ZnIHZpZXdCb3g9XCIwIDAgMTYgMTZcIiB4bWxucz1cImh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnXCI+XG4gICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9XCJNNiAtMy40OTY5MWUtMDdMMTQgOEw2IDE2TDQgMTZMNCAtNC4zNzExNGUtMDdMNiAtMy40OTY5MWUtMDdaXCIgLz5cbiAgICAgICAgICAgICAgICA8L3N2Zz5cbiAgICAgICAgICAgICAgPC9kaXY+XG4gICAgICAgICAgICA8L2Rpdj5cbiAgICAgICAgYDtcclxuICAgIH1cclxuICAgIGNvbm5lY3RlZENhbGxiYWNrKCkge1xyXG4gICAgICAgIHRoaXMudXBkYXRlQ29udGVudCgpO1xyXG4gICAgfVxyXG4gICAgc3RhdGljIGdldCBvYnNlcnZlZEF0dHJpYnV0ZXMoKSB7XHJcbiAgICAgICAgcmV0dXJuIFtcInZhbHVlXCJdO1xyXG4gICAgfVxyXG4gICAgYXR0cmlidXRlQ2hhbmdlZENhbGxiYWNrKG5hbWUsIG9sZFZhbHVlLCBuZXdWYWx1ZSkge1xyXG4gICAgICAgIHRoaXMudXBkYXRlQ29udGVudCgpO1xyXG4gICAgfVxyXG4gICAgdXBkYXRlQ29udGVudCgpIHtcclxuICAgICAgICBjb25zdCB2YWx1ZSA9IHBhcnNlSW50KHRoaXMuZ2V0QXR0cmlidXRlKFwidmFsdWVcIikgfHwgXCJcIik7XHJcbiAgICAgICAgY29uc3QgcGlwcyA9IHRoaXMuc2hhZG93Um9vdC5xdWVyeVNlbGVjdG9yQWxsKFwiLnBpcHMgc3ZnXCIpO1xyXG4gICAgICAgIGZvciAobGV0IGkgPSAwOyBpIDwgcGlwcy5sZW5ndGg7IGkrKykge1xyXG4gICAgICAgICAgICBpZiAodmFsdWUgPiBpKSB7XHJcbiAgICAgICAgICAgICAgICBwaXBzW2ldLmNsYXNzTGlzdC5hZGQoXCJhY3RpdmVcIik7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgZWxzZSB7XHJcbiAgICAgICAgICAgICAgICBwaXBzW2ldLmNsYXNzTGlzdC5yZW1vdmUoXCJhY3RpdmVcIik7XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5QbGF5Ym9va0FjdGlvbkRpc3BsYXlFbGVtZW50Q29tcG9uZW50ID0gUGxheWJvb2tBY3Rpb25EaXNwbGF5RWxlbWVudENvbXBvbmVudDtcclxuY3VzdG9tRWxlbWVudHMuZGVmaW5lKFwiZmJ0Zi1lbGVtZW50LXBsYXlib29rLWFjdGlvbi1kaXNwbGF5XCIsIFBsYXlib29rQWN0aW9uRGlzcGxheUVsZW1lbnRDb21wb25lbnQpO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5leHBvcnRzLlBsYXlib29rQXR0cmlidXRlRGlzcGxheUVsZW1lbnRDb21wb25lbnQgPSB2b2lkIDA7XHJcbmNsYXNzIFBsYXlib29rQXR0cmlidXRlRGlzcGxheUVsZW1lbnRDb21wb25lbnQgZXh0ZW5kcyBIVE1MRWxlbWVudCB7XHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgICAgICBzdXBlcigpO1xyXG4gICAgICAgIGNvbnN0IHNoYWRvd1Jvb3QgPSB0aGlzLmF0dGFjaFNoYWRvdyh7IG1vZGU6IFwib3BlblwiIH0pO1xyXG4gICAgICAgIHNoYWRvd1Jvb3QuaW5uZXJIVE1MID0gYFxuICAgICAgICAgICAgPHN0eWxlPlxuICAgICAgICAgICAgICBmaWd1cmUge1xuICAgICAgICAgICAgICAgIGRpc3BsYXk6ZmxleDtcbiAgICAgICAgICAgICAgICBmbGV4LWRpcmVjdGlvbjpyb3c7XG4gICAgICAgICAgICAgICAganVzdGlmeS1jb250ZW50OiBzcGFjZS1iZXR3ZWVuO1xuICAgICAgICAgICAgICAgIGFsaWduLWl0ZW1zOmNlbnRlcjtcbiAgICAgICAgICAgICAgICBwYWRkaW5nOiAwLjI1cmVtIDA7XG4gICAgICAgICAgICAgICAgbWFyZ2luOiAwLjVyZW0gMCAwO1xuICAgICAgICAgICAgICAgIGZvbnQtd2VpZ2h0OiBib2xkZXI7XG4gICAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgICBmaWd1cmUgc3BhbiB7XG4gICAgICAgICAgICAgICAgZGlzcGxheTogaW5saW5lLWJsb2NrO1xuICAgICAgICAgICAgICAgIGJvcmRlcjogMXB4IHNvbGlkIGJsYWNrO1xuICAgICAgICAgICAgICAgIHBhZGRpbmc6IDAuMjVyZW07XG4gICAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgICBociB7XG4gICAgICAgICAgICAgICAgbWFyZ2luOiAwIDAgMC4yNXJlbTtcbiAgICAgICAgICAgICAgICBwYWRkaW5nOiAwO1xuICAgICAgICAgICAgICB9XG5cbiAgICAgICAgICAgIDwvc3R5bGU+XG4gICAgICAgICAgICA8ZmlndXJlIGNsYXNzPVwiaGVhZGVyXCI+XG4gICAgICAgICAgICAgIDxmaWdjYXB0aW9uPjwvZmlnY2FwdGlvbj5cbiAgICAgICAgICAgICAgPHNwYW4+PC9zcGFuPlxuICAgICAgICAgICAgPC9maWd1cmU+XG4gICAgICAgICAgICA8aHIgLz5cbiAgICAgICAgICAgIDxkaXYgY2xhc3M9J2NoaWxkcmVuJz48c2xvdD48L3Nsb3Q+PC9kaXY+XG4gICAgICAgIGA7XHJcbiAgICB9XHJcbiAgICBjb25uZWN0ZWRDYWxsYmFjaygpIHtcclxuICAgICAgICB0aGlzLnVwZGF0ZUNvbnRlbnQoKTtcclxuICAgIH1cclxuICAgIHN0YXRpYyBnZXQgb2JzZXJ2ZWRBdHRyaWJ1dGVzKCkge1xyXG4gICAgICAgIHJldHVybiBbXCJsYWJlbFwiLCBcInZhbHVlXCJdO1xyXG4gICAgfVxyXG4gICAgYXR0cmlidXRlQ2hhbmdlZENhbGxiYWNrKG5hbWUsIG9sZFZhbHVlLCBuZXdWYWx1ZSkge1xyXG4gICAgICAgIHRoaXMudXBkYXRlQ29udGVudCgpO1xyXG4gICAgfVxyXG4gICAgdXBkYXRlQ29udGVudCgpIHtcclxuICAgICAgICBjb25zdCBsYWJlbCA9IHRoaXMuZ2V0QXR0cmlidXRlKFwibGFiZWxcIikgfHwgXCJcIjtcclxuICAgICAgICB0aGlzLnNoYWRvd1Jvb3QucXVlcnlTZWxlY3RvcihcImZpZ2NhcHRpb25cIikudGV4dENvbnRlbnQgPSBsYWJlbDtcclxuICAgICAgICBjb25zdCB2YWx1ZSA9IHRoaXMuZ2V0QXR0cmlidXRlKFwidmFsdWVcIikgfHwgXCJcIjtcclxuICAgICAgICB0aGlzLnNoYWRvd1Jvb3QucXVlcnlTZWxlY3RvcihcImZpZ3VyZSBzcGFuXCIpLnRleHRDb250ZW50ID0gdmFsdWU7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5QbGF5Ym9va0F0dHJpYnV0ZURpc3BsYXlFbGVtZW50Q29tcG9uZW50ID0gUGxheWJvb2tBdHRyaWJ1dGVEaXNwbGF5RWxlbWVudENvbXBvbmVudDtcclxuY3VzdG9tRWxlbWVudHMuZGVmaW5lKFwiZmJ0Zi1lbGVtZW50LXBsYXlib29rLWF0dHJpYnV0ZS1kaXNwbGF5XCIsIFBsYXlib29rQXR0cmlidXRlRGlzcGxheUVsZW1lbnRDb21wb25lbnQpO1xyXG4iLCJcInVzZSBzdHJpY3RcIjtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiX19lc01vZHVsZVwiLCB7IHZhbHVlOiB0cnVlIH0pO1xyXG5leHBvcnRzLkNlbnRlcmZvbGRMYXlvdXRDb21wb25lbnQgPSB2b2lkIDA7XHJcbmNsYXNzIENlbnRlcmZvbGRMYXlvdXRDb21wb25lbnQgZXh0ZW5kcyBIVE1MRWxlbWVudCB7XHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgICAgICBzdXBlcigpO1xyXG4gICAgICAgIGNvbnN0IHNoYWRvd1Jvb3QgPSB0aGlzLmF0dGFjaFNoYWRvdyh7IG1vZGU6IFwib3BlblwiIH0pO1xyXG4gICAgICAgIHNoYWRvd1Jvb3QuaW5uZXJIVE1MID0gYFxuICAgICAgICA8c3R5bGU+XG4gICAgICAgICAgICAuY29udGVudCB7XG4gICAgICAgICAgICAgICAgZGlzcGxheTpmbGV4O1xuICAgICAgICAgICAgICAgIGZsZXgtZGlyZWN0aW9uOmNvbHVtbjtcbiAgICAgICAgICAgICAgICBqdXN0aWZ5LWNvbnRlbnQ6IGNlbnRlcjtcbiAgICAgICAgICAgICAgICBhbGlnbi1pdGVtczpjZW50ZXI7XG4gICAgICAgICAgICB9XG4gICAgICAgIDwvc3R5bGU+XG4gICAgICAgIDxkaXYgY2xhc3M9XCJjb250ZW50XCI+PHNsb3Q+PC9zbG90PjwvZGl2PlxuICAgIGA7XHJcbiAgICB9XHJcbn1cclxuZXhwb3J0cy5DZW50ZXJmb2xkTGF5b3V0Q29tcG9uZW50ID0gQ2VudGVyZm9sZExheW91dENvbXBvbmVudDtcclxuY3VzdG9tRWxlbWVudHMuZGVmaW5lKFwiZmJ0Zi1sYXlvdXQtY2VudGVyZm9sZFwiLCBDZW50ZXJmb2xkTGF5b3V0Q29tcG9uZW50KTtcclxuIiwiLy8gVGhlIG1vZHVsZSBjYWNoZVxudmFyIF9fd2VicGFja19tb2R1bGVfY2FjaGVfXyA9IHt9O1xuXG4vLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXHQvLyBDaGVjayBpZiBtb2R1bGUgaXMgaW4gY2FjaGVcblx0dmFyIGNhY2hlZE1vZHVsZSA9IF9fd2VicGFja19tb2R1bGVfY2FjaGVfX1ttb2R1bGVJZF07XG5cdGlmIChjYWNoZWRNb2R1bGUgIT09IHVuZGVmaW5lZCkge1xuXHRcdHJldHVybiBjYWNoZWRNb2R1bGUuZXhwb3J0cztcblx0fVxuXHQvLyBDcmVhdGUgYSBuZXcgbW9kdWxlIChhbmQgcHV0IGl0IGludG8gdGhlIGNhY2hlKVxuXHR2YXIgbW9kdWxlID0gX193ZWJwYWNrX21vZHVsZV9jYWNoZV9fW21vZHVsZUlkXSA9IHtcblx0XHQvLyBubyBtb2R1bGUuaWQgbmVlZGVkXG5cdFx0Ly8gbm8gbW9kdWxlLmxvYWRlZCBuZWVkZWRcblx0XHRleHBvcnRzOiB7fVxuXHR9O1xuXG5cdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuXHRfX3dlYnBhY2tfbW9kdWxlc19fW21vZHVsZUlkXShtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuXHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuXHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG59XG5cbiIsIlwidXNlIHN0cmljdFwiO1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJfX2VzTW9kdWxlXCIsIHsgdmFsdWU6IHRydWUgfSk7XHJcbmV4cG9ydHMuQ2VudGVyZm9sZExheW91dENvbXBvbmVudCA9IGV4cG9ydHMuUGxheWJvb2tBdHRyaWJ1dGVEaXNwbGF5RWxlbWVudENvbXBvbmVudCA9IGV4cG9ydHMuUGxheWJvb2tBY3Rpb25EaXNwbGF5RWxlbWVudENvbXBvbmVudCA9IHZvaWQgMDtcclxudmFyIHBsYXlib29rX2FjdGlvbl9kaXNwbGF5XzEgPSByZXF1aXJlKFwiLi9jb21wb25lbnRzL2VsZW1lbnRzL3BsYXlib29rLWFjdGlvbi1kaXNwbGF5XCIpO1xyXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgXCJQbGF5Ym9va0FjdGlvbkRpc3BsYXlFbGVtZW50Q29tcG9uZW50XCIsIHsgZW51bWVyYWJsZTogdHJ1ZSwgZ2V0OiBmdW5jdGlvbiAoKSB7IHJldHVybiBwbGF5Ym9va19hY3Rpb25fZGlzcGxheV8xLlBsYXlib29rQWN0aW9uRGlzcGxheUVsZW1lbnRDb21wb25lbnQ7IH0gfSk7XHJcbnZhciBwbGF5Ym9va19hdHRyaWJ1dGVfZGlzcGxheV8xID0gcmVxdWlyZShcIi4vY29tcG9uZW50cy9lbGVtZW50cy9wbGF5Ym9vay1hdHRyaWJ1dGUtZGlzcGxheVwiKTtcclxuT2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIFwiUGxheWJvb2tBdHRyaWJ1dGVEaXNwbGF5RWxlbWVudENvbXBvbmVudFwiLCB7IGVudW1lcmFibGU6IHRydWUsIGdldDogZnVuY3Rpb24gKCkgeyByZXR1cm4gcGxheWJvb2tfYXR0cmlidXRlX2Rpc3BsYXlfMS5QbGF5Ym9va0F0dHJpYnV0ZURpc3BsYXlFbGVtZW50Q29tcG9uZW50OyB9IH0pO1xyXG52YXIgY2VudGVyZm9sZF8xID0gcmVxdWlyZShcIi4vY29tcG9uZW50cy9sYXlvdXRzL2NlbnRlcmZvbGRcIik7XHJcbk9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBcIkNlbnRlcmZvbGRMYXlvdXRDb21wb25lbnRcIiwgeyBlbnVtZXJhYmxlOiB0cnVlLCBnZXQ6IGZ1bmN0aW9uICgpIHsgcmV0dXJuIGNlbnRlcmZvbGRfMS5DZW50ZXJmb2xkTGF5b3V0Q29tcG9uZW50OyB9IH0pO1xyXG4iXSwibmFtZXMiOltdLCJzb3VyY2VSb290IjoiIn0=