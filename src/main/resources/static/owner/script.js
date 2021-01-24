const productCard = {
	toogleCardImg: () => {
		for(let o of document.querySelectorAll(".card-img-top-focus")) {
			o.onmouseenter = o.onmouseleave = () => {
				let childrens = o.children;

				// Toggle images of the card
				childrens[0].classList.toggle("d-none");
				childrens[1].classList.toggle("d-none");
			};
		}
	}
};

const header = {
	dropdownMenuHover: () => {
		const userDropDown = document.querySelector("#user-dropdown");

		userDropDown.onmouseenter = userDropDown.onmouseleave = () => {
			$("#user-dropdown-link").dropdown("toggle");
		}
	},

	searchInputPredictionsPopover: () => {
		let text = "";
		const searchInput = document.querySelector("#search-input");

		searchInput.onkeyup = () => {
			const value = searchInput.value;

			if(value.length < 3) {
				$("#search-input").popover("hide");
				return;
			}

			text = `<a class='d-block cursor-pointer-hover mb-2' href='/base.html'>${value}</a>`.repeat(10);

			searchInput.setAttribute("data-content", text);

			$("#search-input").popover("show");
		};
	}
};

const wishlist = {
	wishlistCount: (action) => {
		let wishlistBadge = document.querySelector("#wishlist-badg");
		let value = wishlistBadge.innerText.length == 0 ? 0 : parseInt(wishlistBadge.innerText);

		if(action === "add") value += 1;
		else if(action === "remove") value -= 1;
		else return;

		wishlistBadge.innerText = value == 0 ? "" : value;
	},

	wishlistCookieValueToArray: () => {
		let cookieValue = /(?<=wishlistProducts=)[0-9.]+/ig.exec(document.cookie);

		if(cookieValue) return cookieValue["0"].split(/\./).map(e => parseInt(e));
		else return [];
	}
};

const cookies = {
	setWishlistCookies: (productId) => {
		let wishlistCodes = wishlistCookieValueToArray();
		let nextCookie = "";

		if(wishlistCodes.length > 0) {
			if(wishlistCodes.includes(parseInt(productId)))
				nextCookie = wishlistCodes.filter(e => e != productId).join(".");
			else nextCookie = wishlistCodes.join(".")+"."+productId;
		}
		else nextCookie = productId;

		document.cookie = `wishlistProducts=${nextCookie}; path=/; max-age=${3600 * 24}`;
	}
};


const description = {
	validateFreteInput: () => {
		let freteInput = document.querySelector("#frete-input");

		freteInput.onkeyup = () => {
			let newStr = "";

			for(let s of freteInput.value)
				if(/^\d$/ig.test(s)) newStr += s;

			freteInput.value = newStr;
		};
	},

	carouselTrick: () => {
		const carousel = document.querySelector("#products-carousel");

		carousel.onmouseenter = () => carousel.classList.remove("carousel-fade");
		carousel.onmouseleave = () => carousel.classList.add("carousel-fade");
	},

	wishlistAddBtn: () => {
		const btn = document.querySelector("#wishlistAddBtn");
		btn.onclick = () => wishlist.wishlistCount("add");
	}
};